# Thistle

> Kotlin multiplatform String markup library, inspired by [SRML](https://github.com/jasonwyatt/SRML). Thistle is a 
> common parser which produces an AST that can be rendered to a variety of platform-specific UIs.

![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/thistle)
![Maven Central](https://img.shields.io/maven-central/v/io.github.copper-leaf/thistle-core)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.4.32-orange)

```kotlin
// Android
val thistle = ThistleParser(AndroidDefaults)
binding.textView.applyStyledText(
    thistle,
    "This is a {{b}}very important{{/b}}, {{foreground color=#ff0000}}urgent{{/foreground}} message!"
)

// Console
val thistle = ThistleParser(ConsoleDefaults)
binding.textView.applyStyledText(
    thistle,
    "This is a {{b}}very important{{/b}}, {{red}}urgent{{/red}} message!"
)
```

![sample_app](https://copper-leaf.github.io/thistle/assets/media/sample_app.gif)
[View sample app source](https://github.com/copper-leaf/thistle/tree/master/app)

![sample_console](https://copper-leaf.github.io/thistle/assets/media/sample_console.png)

# Supported Platforms/Features

| Platform   | Markup Target               |
| ---------- | --------------------------- |
| Android    | android.text.Spannable      |
| Compose UI | AttributedString (TODO)     |
| iOS        | NSAttributedString (TODO)   |
| JS         | HTML DOM (TODO)             |
| Any        | HTML Text (TODO)            |
| Any        | HTML Console ANSI Sequences |

# Installation

```kotlin
repositories {
    mavenCentral()
}

// for plain JVM or Android projects
dependencies {
    implementation("io.github.copper-leaf:thistle-core:{{site.version}}")
}

// for multiplatform projects
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.copper-leaf:thistle-core:{{site.version}}")
            }
        }
    }
}
```

# Documentation

See the [website](https://copper-leaf.github.io/thistle/) for detailed documentation and usage instructions.

# License 

Thistle is licensed under the BSD 3-Clause License, see [LICENSE.md](https://github.com/copper-leaf/thistle/tree/master/LICENSE.md). 

# References

- [SRML](https://github.com/jasonwyatt/SRML)
