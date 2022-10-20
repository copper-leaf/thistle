@file:Suppress("UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    kotlin("multiplatform")
    id("io.kotest.multiplatform")
}

val libs = the<LibrariesForLibs>()
val customProperties = Config.customProperties(project)

kotlin {
    // sourcesets
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies { }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlinx.coroutines.test)

                implementation(libs.kotest.engine)
                implementation(libs.kotest.assertions)
                implementation(libs.kotest.dataTest)
                implementation(libs.kotest.propertyTest)
            }
        }

        if (customProperties["copperleaf.targets.jvm"] == true) {
            val jvmMain by getting {
                dependencies { }
            }
            val jvmTest by getting {
                dependencies {
                    implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
                    runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
                }
            }
        }

        // Android JVM Sourcesets
        if (customProperties["copperleaf.targets.android"] == true) {
            val androidMain by getting {
                dependencies { }
            }
            val androidTest by getting {
                dependencies {
                    implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
                    runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
                }
            }
        }

        if (customProperties["copperleaf.targets.js"] == true) {
            // JS Sourcesets
            val jsMain by getting {
                dependencies { }
            }
            val jsTest by getting {
                dependencies { }
            }
        }

        if (customProperties["copperleaf.targets.ios"] == true) {
            // iOS Sourcesets
            val iosMain by getting {
                dependencies { }
            }
            val iosTest by getting {
                dependencies { }
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = false
    }
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
