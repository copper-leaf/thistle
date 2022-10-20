plugins {
    `copper-leaf-android`
    `copper-leaf-targets`
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-testing`
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
    }
}
