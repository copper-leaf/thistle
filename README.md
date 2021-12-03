# Thistle

> Kotlin multiplatform String markup library, inspired by [SRML](https://github.com/jasonwyatt/SRML). Thistle is a 
> common parser which produces an AST that can be rendered to a variety of platform-specific UIs.

![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/thistle)
![Maven Central](https://img.shields.io/maven-central/v/io.github.copper-leaf/thistle-core)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.5.31-orange)

```kotlin
// Android
val thistle = ThistleParser(AndroidDefaults(context))
binding.textView.applyStyledText(
    thistle,
    "Text with {{b}}bold{{/b}} or {{foreground color=#ff0000}}red{{/foreground}} styles"
)


// Compose (both Android and Desktop)
MaterialTheme {
    ProvideThistle {
        StyledText("Text with {{b}}bold{{/b}} or {{foreground color=#ff0000}}red{{/foreground}} styles")
    }
}


// Console (ANSI codes)
val thistle = ThistleParser(ConsoleDefaults())
printlnStyledText(
    thistle,
    "Text with {{b}}bold{{/b}} or {{red}}red{{/red}} styles"
)
```

| Android   | Compose UI | Console |
| --------- | ---------- | ------- |
| ![sample_app](https://github.com/copper-leaf/thistle/blob/main/docs/src/orchid/resources/assets/media/sample_android.gif?raw=true) | ![sample_console](https://copper-leaf.github.io/thistle/assets/media/sample_compose.png) | ![sample_console](https://copper-leaf.github.io/thistle/assets/media/sample_console.png) |
| [Source](https://github.com/copper-leaf/thistle/tree/main/examples/android) | [Source](https://github.com/copper-leaf/thistle/tree/main/examples/compose-desktop) | [Source](https://github.com/copper-leaf/thistle/blob/main/thistle-console/src/commonTest/kotlin/com/copperleaf/thistle/console/TestConsoleRenderer.kt) | 

# Supported Platforms/Features

| Platform   | Markup Target               |
| ---------- | --------------------------- |
| Android    | android.text.Spannable      |
| Compose UI | AttributedString            |
| Console    | ANSI Codes                  |
| iOS        | NSAttributedString (TODO)   |
| JS         | HTML DOM (TODO)             |

# Installation

```kotlin
repositories {
    mavenCentral()
}

// for plain JVM or Android projects
dependencies {
    implementation("io.github.copper-leaf:thistle-core:{{site.version}}")
    implementation("io.github.copper-leaf:thistle-android:{{site.version}}")
    implementation("io.github.copper-leaf:thistle-compose-ui:{{site.version}}")
    implementation("io.github.copper-leaf:thistle-console:{{site.version}}")
}

// for multiplatform projects
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.copper-leaf:thistle-core:{{site.version}}")
                implementation("io.github.copper-leaf:thistle-android:{{site.version}}")
                implementation("io.github.copper-leaf:thistle-compose-ui:{{site.version}}")
                implementation("io.github.copper-leaf:thistle-console:{{site.version}}")
            }
        }
    }
}
```

# Documentation

See the [website](https://copper-leaf.github.io/thistle/) for detailed documentation and usage instructions.

# License 

Thistle is licensed under the BSD 3-Clause License, see [LICENSE.md](https://github.com/copper-leaf/thistle/tree/main/LICENSE.md). 

# References

- [SRML](https://github.com/jasonwyatt/SRML)
