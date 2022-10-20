@file:Suppress("UNUSED_VARIABLE")

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val libs = the<LibrariesForLibs>()
val customProperties = Config.customProperties(project)

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies { }
        }
        val commonTest by getting {
            dependencies { }
        }

        if (customProperties["copperleaf.targets.jvm"] == true) {
            val jvmMain by getting {
                dependencies { }
            }
            val jvmTest by getting {
                dependencies { }
            }
        }

        // Android JVM Sourcesets
        if (customProperties["copperleaf.targets.android"] == true) {
            val androidMain by getting {
                dependencies { }
            }
            val androidTest by getting {
                dependencies { }
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

