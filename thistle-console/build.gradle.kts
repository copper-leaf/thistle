plugins {
    `copper-leaf-android`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
}

description = "Kotlin Multiplatform String markup and formatting utility"

kotlin {
    sourceSets {
        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":thistle-core"))
            }
        }
        val commonTest by getting {
            dependencies { }
        }

        // plain JVM Sourcesets
        val jvmMain by getting {
            dependencies { }
        }
        val jvmTest by getting {
            dependencies { }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies { }
        }
        val androidTest by getting {
            dependencies { }
        }

        // JS Sourcesets
        val jsMain by getting {
            dependencies { }
        }
        val jsTest by getting {
            dependencies { }
        }

        // iOS Sourcesets
        val iosMain by getting {
            dependencies { }
        }
        val iosTest by getting {
            dependencies { }
        }
    }
}
