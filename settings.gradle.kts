pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "binary-compatibility-validator" -> useModule("org.jetbrains.kotlinx:binary-compatibility-validator:${requested.version}")
            }
        }
    }

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
}

rootProject.name = "thistle"

include(":thistle-core")
include(":thistle-android")
include(":thistle-console")
include(":thistle-compose-jetbrains")

include(":examples:android")
include(":examples:compose-desktop")

//include(":docs")
