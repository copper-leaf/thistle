@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    kotlin("multiplatform")
    id("io.kotest.multiplatform")
}

val libs = the<LibrariesForLibs>()

kotlin {
    explicitApi()

    // targets
    jvm { }
    android {
        publishAllLibraryVariants()
    }
    js(BOTH) {
        browser {
            testTask {
                enabled = false
            }
        }
    }
    nativeTargetGroup(
        "ios",
        iosArm32(),
        iosArm64(),
        iosX64(),
        iosSimulatorArm64(),
    )

    // sourcesets
    sourceSets {
        all {
            languageSettings.apply {
            }
        }

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

        val jvmMain by getting {
            dependencies { }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies { }
        }
        val androidTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
            }
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

tasks.withType<JavaCompile> {
    sourceCompatibility = Config.javaVersion
    targetCompatibility = Config.javaVersion
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
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = Config.javaVersion
    }
}
