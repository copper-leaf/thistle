plugins {
    id("com.eden.orchidPlugin") version "0.21.1"
    `copper-leaf-base`
    `copper-leaf-version`
    `copper-leaf-lint`
}

repositories {
    jcenter()
}

dependencies {
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidDocs:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidCopper:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidGithub:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidKotlindoc:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidPluginDocs:0.21.1")
    orchidRuntimeOnly("io.github.javaeden.orchid:OrchidSnippets:0.21.1")
}

// Orchid setup
// ---------------------------------------------------------------------------------------------------------------------

val ghUser: String by extra
val ghToken: String by extra
val releaseVersion: String by extra

orchid {
    githubToken = ghToken
    version = releaseVersion
}

val build by tasks
val check by tasks
val orchidBuild by tasks
val orchidDeploy by tasks

orchidBuild.mustRunAfter(check)
build.dependsOn(orchidBuild)

val publish by tasks.registering {
    dependsOn(orchidDeploy)
}
