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

import org.gradle.api.artifacts.Configuration
import org.gradle.kotlin.dsl.exclude

object Dependencies {

  object Androidx {
    const val activity = "androidx.activity:activity:${Versions.Androidx.activity}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.Androidx.appCompat}"
    const val constraintLayout =
      "androidx.constraintlayout:constraintlayout:${Versions.Androidx.constraintLayout}"
    const val datastorePref =
      "androidx.datastore:datastore-preferences:${Versions.Androidx.datastorePref}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.Androidx.fragmentKtx}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.Androidx.recyclerView}"
    const val workRuntimeKtx = "androidx.work:work-runtime-ktx:${Versions.Androidx.workRuntimeKtx}"
  }

  object Kotlin {
    const val kotlinCoroutinesAndroid =
      "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.kotlinCoroutinesCore}"
    const val kotlinCoroutinesCore =
      "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.kotlinCoroutinesCore}"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Kotlin.stdlib}"
  }

  object Lifecycle {
    const val liveDataKtx =
      "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Androidx.lifecycle}"
    const val runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.Androidx.lifecycle}"
    const val viewModelKtx =
      "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Androidx.lifecycle}"
  }

  object Navigation {
    const val navFragmentKtx =
      "androidx.navigation:navigation-fragment-ktx:${Versions.Androidx.navigation}"
    const val navUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.Androidx.navigation}"
  }

  object Retrofit {
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
  }

  const val appAuth = "net.openid:appauth:${Versions.appAuth}"
  const val jwtDecode = "com.auth0.android:jwtdecode:${Versions.jwtDecode}"
  const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugarJdkLibs}"
  const val http = "com.squareup.okhttp3:okhttp:${Versions.http}"
  const val kotlinPoet = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"
  const val material = "com.google.android.material:material:${Versions.material}"
  const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

  const val androidJunitRunner = "androidx.test.runner.AndroidJUnitRunner"

  const val androidFhirGroup = "com.google.android.fhir"
  const val androidFhirDataCaptureModule = "data-capture"
  const val androidFhirEngineModule = "engine"
  const val androidFhirKnowledgeModule = "knowledge"
  const val androidFhirDataCapture =
          "$androidFhirGroup:$androidFhirDataCaptureModule:${Versions.androidFhirDataCapture}"
  const val androidFhirEngine =
          "$androidFhirGroup:$androidFhirEngineModule:${Versions.androidFhirEngine}"
  const val androidFhirKnowledge =
          "$androidFhirGroup:$androidFhirKnowledgeModule:${Versions.androidFhirKnowledge}"

  object Versions {
    object Androidx {
      const val activity = "1.2.1"
      const val appCompat = "1.1.0"
      const val constraintLayout = "2.1.1"
      const val datastorePref = "1.0.0"
      const val fragmentKtx = "1.3.1"
      const val lifecycle = "2.2.0"
      const val navigation = "2.3.4"
      const val recyclerView = "1.1.0"
      const val workRuntimeKtx = "2.7.1"
    }

    object Kotlin {
      const val kotlinCoroutinesCore = "1.4.2"
      const val stdlib = "1.6.10"
    }

    const val appAuth = "0.11.1"
    const val desugarJdkLibs = "1.1.5"
    const val http = "4.9.1"
    const val jwtDecode = "2.0.1"
    const val kotlinPoet = "1.9.0"
    const val material = "1.6.0"
    const val retrofit = "2.7.2"
    const val timber = "5.0.1"

    const val androidFhirDataCapture = "1.0.0"
    const val androidFhirEngine = "0.1.0-beta03"
    const val androidFhirKnowledge = "0.1.0-alpha01"

    // Hapi FHIR and HL7 Core Components are interlinked.
    // Newer versions of HapiFhir don't work on Android due to the use of Caffeine 3+
    // Wait for this to release (6.3): https://github.com/hapifhir/hapi-fhir/pull/4196
    const val hapiFhir = "6.0.1"
    // Newer versions don't work on Android due to Apache Commons Codec:
    // Wait for this fix: https://github.com/hapifhir/org.hl7.fhir.core/issues/1046
    const val hapiFhirCore = "5.6.36"
    const val caffeine = "2.9.1"

  }

  object HapiFhir {
    const val fhirBase = "ca.uhn.hapi.fhir:hapi-fhir-base:${Versions.hapiFhir}"
    const val fhirClient = "ca.uhn.hapi.fhir:hapi-fhir-client:${Versions.hapiFhir}"
    const val structuresDstu2 = "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu2:${Versions.hapiFhir}"
    const val structuresDstu3 = "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu3:${Versions.hapiFhir}"
    const val structuresR4 = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4:${Versions.hapiFhir}"
    const val structuresR4b = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4b:${Versions.hapiFhir}"
    const val structuresR5 = "ca.uhn.hapi.fhir:hapi-fhir-structures-r5:${Versions.hapiFhir}"

    const val validation = "ca.uhn.hapi.fhir:hapi-fhir-validation:${Versions.hapiFhir}"
    const val validationDstu3 =
      "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-dstu3:${Versions.hapiFhir}"
    const val validationR4 =
      "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r4:${Versions.hapiFhir}"
    const val validationR5 =
      "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r5:${Versions.hapiFhir}"

    const val fhirCoreDstu2 = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu2:${Versions.hapiFhirCore}"
    const val fhirCoreDstu2016 =
      "ca.uhn.hapi.fhir:org.hl7.fhir.dstu2016may:${Versions.hapiFhirCore}"
    const val fhirCoreDstu3 = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu3:${Versions.hapiFhirCore}"
    const val fhirCoreR4 = "ca.uhn.hapi.fhir:org.hl7.fhir.r4:${Versions.hapiFhirCore}"
    const val fhirCoreR4b = "ca.uhn.hapi.fhir:org.hl7.fhir.r4b:${Versions.hapiFhirCore}"
    const val fhirCoreR5 = "ca.uhn.hapi.fhir:org.hl7.fhir.r5:${Versions.hapiFhirCore}"
    const val fhirCoreUtils = "ca.uhn.hapi.fhir:org.hl7.fhir.utilities:${Versions.hapiFhirCore}"
    const val fhirCoreConvertors =
      "ca.uhn.hapi.fhir:org.hl7.fhir.convertors:${Versions.hapiFhirCore}"

    // Runtime dependency that is required to run FhirPath (also requires minSDK of 26).
    // Version 3.0 uses java.lang.System.Logger, which is not available on Android
    // Replace for Guava when this PR gets merged: https://github.com/hapifhir/hapi-fhir/pull/3977
    const val caffeine = "com.github.ben-manes.caffeine:caffeine:${Versions.caffeine}"
  }

  fun Configuration.removeIncompatibleDependencies() {
    exclude(module = "xpp3")
    exclude(module = "xpp3_min")
    exclude(module = "xmlpull")
    exclude(module = "javax.json")
    exclude(module = "jcl-over-slf4j")
    exclude(group = "org.apache.httpcomponents")
  }

  fun Configuration.forceHapiVersion() {
    // Removes newer versions of caffeine and manually imports 2.9
    // Removes newer versions of hapi and keeps on 6.0.1
    // Removes newer versions of HL7 core and keeps it on 5.6.36
    // (newer versions don't work on Android)
    resolutionStrategy {
      force(HapiFhir.caffeine)

      force(HapiFhir.fhirBase)
      force(HapiFhir.fhirClient)
      force(HapiFhir.fhirCoreConvertors)

      force(HapiFhir.fhirCoreDstu2)
      force(HapiFhir.fhirCoreDstu2016)
      force(HapiFhir.fhirCoreDstu3)
      force(HapiFhir.fhirCoreR4)
      force(HapiFhir.fhirCoreR4b)
      force(HapiFhir.fhirCoreR5)
      force(HapiFhir.fhirCoreUtils)

      force(HapiFhir.structuresDstu2)
      force(HapiFhir.structuresDstu3)
      force(HapiFhir.structuresR4)
      force(HapiFhir.structuresR5)

      force(HapiFhir.validation)
      force(HapiFhir.validationDstu3)
      force(HapiFhir.validationR4)
      force(HapiFhir.validationR5)
    }
  }
}
