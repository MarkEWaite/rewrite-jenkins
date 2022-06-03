/*
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.jenkins;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.maven.ChangeParentPom;
import org.openrewrite.maven.ChangePropertyValue;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.xml.ChangeTagValueVisitor;
import org.openrewrite.xml.tree.Xml;

public class ChangeJenkinsVersion extends Recipe {

    @Override
    public String getDisplayName() {
        return "Use `new ArrayList<>()` instead of Guava";
    }

    @Override
    public String getDescription() {
        return "Prefer the Java standard library over third-party usage of Guava in simple cases like this.";
    }



    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new MavenVisitor<ExecutionContext>() {

            @Override
            public Xml visitTag(Xml.Tag tag, ExecutionContext executionContext) {
                if (isParentTag()) {
                    return  (Xml.Tag) new ChangeTagValueVisitor<>(tag.getChild("version").get(), "4.40").visitNonNull(tag, executionContext);
                }
                return super.visitTag(tag, executionContext);
            }

            @Override
            public Xml visitDocument(Xml.Document document, ExecutionContext executionContext) {
                Xml.Document doc = (Xml.Document) new ChangePropertyValue("jenkins.version", "2.303.3", true).getVisitor().visitNonNull(document, executionContext);
                doc = (Xml.Document) super.visitDocument(doc, executionContext);


                // ast now changed after calling visit
                if (doc != document) { // have we changed it
                    doAfterVisit(new MavenVisitor<ExecutionContext>() {
                        @Override
                        public Xml visitDocument(Xml.Document document, ExecutionContext executionContext) {
                            // uncomment to reproduce failure
//                            maybeUpdateModel();
                            return document;
                        }
                    });
                }
                return doc;
            }
        };
    }
}
