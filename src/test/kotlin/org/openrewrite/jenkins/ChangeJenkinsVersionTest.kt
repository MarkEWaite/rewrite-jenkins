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
package org.openrewrite.jenkins

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrewrite.maven.tree.MavenResolutionResult
import org.openrewrite.test.RecipeSpec
import org.openrewrite.test.RewriteTest

class ChangeJenkinsVersionTest: RewriteTest {

    //Note, you can define defaults for the RecipeSpec and these defaults will be used for all tests.
    //In this case, the recipe and the parser are common. See below, on how the defaults can be overriden
    //per test.
    override fun defaults(spec: RecipeSpec) {
        spec
            .recipe(ChangeJenkinsVersion())
    }

    @Test
    fun replaceWithNewArrayList() = rewriteRun(
            pomXml(
                    """
                        <project>
                            <parent>
                                <groupId>org.jenkins-ci.plugins</groupId>
                                <artifactId>plugin</artifactId>
                                <version>3.40</version>
                            </parent>
                            <artifactId>permissive-script-security</artifactId>
                            <version>0.8-SNAPSHOT</version>

                            <properties>
                                <jenkins.version>2.107.3</jenkins.version>
                            </properties>

                            <repositories>
                                <repository>
                                    <id>repo.jenkins-ci.org</id>
                                    <url>https://repo.jenkins-ci.org/public/</url>
                                </repository>
                            </repositories>
                        </project>
                    """, """
                        <project>
                            <parent>
                                <groupId>org.jenkins-ci.plugins</groupId>
                                <artifactId>plugin</artifactId>
                                <version>4.40</version>
                            </parent>
                            <artifactId>permissive-script-security</artifactId>
                            <version>0.8-SNAPSHOT</version>

                            <properties>
                                <jenkins.version>2.303.3</jenkins.version>
                            </properties>

                            <repositories>
                                <repository>
                                    <id>repo.jenkins-ci.org</id>
                                    <url>https://repo.jenkins-ci.org/public/</url>
                                </repository>
                            </repositories>
                        </project>
                    """
//                    ,
//                    { spec ->
////                        spec.afterRecipe({ pom -> val result = pom.markers.findFirst(MavenResolutionResult::class.java).get()
////                            assertThat(result.parent.pom.version).isEqualTo("4.40")
////                        })
//                    }
            )
    )
}
