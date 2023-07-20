/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.api.publish.maven.MavenPom

object Releases {
  const val groupId = "com.google.fhir.examples"

  // Demo apps
  object Demo {
    const val applicationId = "com.google.fhir.examples.demo"
    const val versionCode = 1
    const val versionName = "1.0"
  }

  object ConfigurableCare {
    const val applicationId = "com.google.fhir.examples.configurablecare"
    const val versionCode = 1
    const val versionName = "1.0"
  }

  fun MavenPom.useApache2License() {
    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
      }
    }
  }
}
