plugins {
    `copper-leaf-base`
    `copper-leaf-version`
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.8.0"
    `copper-leaf-sonatype`
}

apiValidation {
    /**
     * Sub-projects that are excluded from API validation
     */
    ignoredProjects.addAll(listOf("examples"))
}
