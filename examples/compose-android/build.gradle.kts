plugins {
    `copper-leaf-base`
    `copper-leaf-lint`
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        applicationId = "com.copperleaf.thistle.composeandroid"
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"

        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.5"
    }
    buildFeatures {
        compose = true
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
    implementation(project(":thistle-compose-ui"))

    // Compose
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.material:material:1.0.5")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}
