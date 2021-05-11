---
---

## Thistle

Kotlin multiplatform String markup library, inspired by [SRML](https://github.com/jasonwyatt/SRML).

![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/thistle)
![Maven Central](https://img.shields.io/maven-central/v/io.github.copper-leaf/thistle-core)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.4.32-orange)

**Example Usage**

```kotlin
val thistle = ThistleParser {
    from(DefaultAndroidTags)
}
binding.textView.applyStyledText(
    thistle,
    "This is a {{b}}very important{{/b}}, {{foreground color=#ff0000}}urgent{{/foreground}} message!"
)
```

![sample_app](https://copper-leaf.github.io/thistle/assets/media/sample_app.gif)

[View sample app source](https://github.com/copper-leaf/thistle/tree/master/app)

### Supported Platforms/Features

| Platform | Markup Target             |
| -------- | ------------------------- |
| Android  | android.text.Spannable    |
| iOS      | NSAttributedString (TODO) |
| JS       | <span> (TODO)             |

### Installation

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
