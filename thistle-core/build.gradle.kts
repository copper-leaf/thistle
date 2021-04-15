plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
    `copper-leaf-publish`
}

description = "Multiplatform String markup"

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
    android {
        publishAllLibraryVariants()
    }
//    js(BOTH) {
//        browser { }
//    }
//    ios { }

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.Experimental")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                api("io.github.copper-leaf:kudzu-core:2.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:1.4.32")
            }
        }

        // Android JVM Sourcesets
        val androidMain by getting {
            dependencies {
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.32")
                implementation("io.mockk:mockk:1.11.0")
            }
        }

//        // JS Sourcesets
//        val jsMain by getting {
//            dependencies {
//            }
//        }
//        val jsTest by getting {
//            dependencies {
//                implementation("org.jetbrains.kotlin:kotlin-test-js:1.4.32")
//            }
//        }
//
//        // iOS Sourcesets
//        val iosMain by getting {
//            dependencies { }
//        }
//        val iosTest by getting {
//            dependencies { }
//        }
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
