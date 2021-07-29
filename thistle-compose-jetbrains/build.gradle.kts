plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
    id("org.jetbrains.compose")
}

description = "Thistle extensions for Jetbrains Compose (Android and Desktop)"

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = project.version.toString()
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
        }
    }
    sourceSets {
        val main by getting {
            setRoot("src/androidMain")
        }
        val androidTest by getting {
            setRoot("src/androidTest")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    lintOptions {
        disable("GradleDependency")
    }
}

kotlin {
    jvm { }
    android {
        publishAllLibraryVariants()
    }

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.Experimental")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api(project(":thistle-core"))
                api("io.github.copper-leaf:kudzu-core:2.6.0")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("io.mockk:mockk:1.11.0")
                implementation("org.jetbrains.compose.ui:ui-test-junit4:0.5.0-build270")
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
            }
        }
        val androidAndroidTestRelease by getting { }
        val androidTest by getting {
            dependsOn(androidAndroidTestRelease)
            dependencies {
            }
        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = Config.javaVersion
    targetCompatibility = Config.javaVersion
}
tasks.withType<Test> {
    testLogging {
        showStandardStreams = true
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.useIR = true
    kotlinOptions {
        jvmTarget = Config.javaVersion
    }
}
