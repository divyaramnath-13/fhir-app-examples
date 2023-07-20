import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.9.0"
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  val kotlin_version by extra("1.9.0")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  dependencies {
    classpath(Plugins.androidGradlePlugin)
    classpath(Plugins.kotlinGradlePlugin)
    classpath(Plugins.navSafeArgsGradlePlugin)
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = uri("${project.rootDir}/dependencies"))

    gradlePluginPortal()
  }
  configureSpotless()
}

subprojects { configureLicensee() }

// Create a CI repository and also change versions to include the build number
afterEvaluate {
  val buildNumber = System.getenv("GITHUB_RUN_ID")
  if (buildNumber != null) {
    subprojects {
      apply(plugin = Plugins.BuildPlugins.mavenPublish)
      configure<PublishingExtension> {
        repositories {
          maven {
            name = "CI"
            url = uri("file://${rootProject.buildDir}/ci-repo")
          }
        }
        // update version to have suffix of build id
        project.version = "${project.version}-build_$buildNumber"
      }
    }
  }
}
dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("androidx.appcompat:appcompat:1.6.1")
}
repositories {
  mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  jvmTarget = "1.8"
}