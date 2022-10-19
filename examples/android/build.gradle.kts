plugins {
    `copper-leaf-base`
    `copper-leaf-lint`
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 32
    defaultConfig {
        minSdk = 21
        targetSdk = 32
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        applicationId = "com.copperleaf.thistle.android"
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    lintOptions {
        disable(
            "GradleDependency",
            "AllowBackup",
            "HardcodedText",
            "ObsoleteLintCustomCheck"
        )
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
    implementation("androidx.fragment:fragment-ktx:1.3.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01")

    implementation(project(":thistle-core"))
    implementation(project(":thistle-android"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
