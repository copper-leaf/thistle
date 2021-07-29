plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.1.0")
    implementation("org.jetbrains.compose:compose-gradle-plugin:0.5.0-build270")
}
