---
---

## Thistle

Kotlin multiplatform String markup library, inspired by [SRML](https://github.com/jasonwyatt/SRML).

![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/thistle)
![Maven Central](https://img.shields.io/maven-central/v/io.github.copper-leaf/thistle-core)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.4.32-orange)

### Supported Platforms/Features

| Platform | Markup Target             |
| -------- | ------------------------- |
| Android  | android.text.Spannable    |
| iOS      | NSAttributedString (TODO) |
| JS       | <span> (TODO)             |

{.table}

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

## Usage

### Android

#### Basic Usage

On Android, Thistle parses a String into a `Spanned` instance that can be set to a TextView. The Thistle format replaces
"tags" with Android `Span`s, wrapping the appropriate text. The normal Span API can be a bit of a pain, and Thistle
makes this simpler, and also allows you to change the span formatting at runtime rather than compile time.

```kotlin
// create the Thistle parser. It's best to create this once and inject it wherever needed
val thistle = ThistleParser(
    ThistleSyntax.builder {
        // add the default tags for Android
        from(DefaultAndroidTags)

        // add any custom tags you need
        tag("inc") { Link { widget: View -> /* do something on link-click */ } }
        tag("dec") { Link { widget: View -> /* do something on link-click */ } }
    }
)

// parse a formatted string to a Spanned instance, and set that as the text of a TextView
val tv: TextView = ...
tv.applyStyledText(
    thistle,
    "{{inc}}Click Me!{{/inc}}    {{foreground color=#ff0000}}|{{/foreground}}    {{dec}}Don't Click Me!{{/dec}}"
)
```

**Default Tags**

| Tag Name      | Params                            | Description                                                        | Example |
| ------------- | --------------------------------- | ------------------------------------------------------------------ | ------- |
| foreground    | `color=[hex color]`               | Change text color                                                  | `{{foreground color=#FFFF00}}Text{{/foreground}}`    |
| background    | `color=[hex color]`               | Change background color                                            | `{{background color=#FFFF00}}Text{{/background}}`    |
| style         | `style=[bold,italic]`             | Set text to bold or italic by argument                             | `{{style style=bold}}Text{{/style}}`                 |
| b             | none                              | Set text style to bold                                             | `{{b}}Text{{/b}}`                                    |
| i             | none                              | Set text style to italic                                           | `{{i}}Text{{/i}}`                                    |
| u             | none                              | Add underline to text                                              | `{{u}}Text{{/u}}`                                    |
| strikethrough | none |                            | Add strikethrough to text                                          | `{{strikethrough}}Text{{/strikethrough}}`            |
| typeface      | `typeface=[monospace,sans,serif]` | Change the typeface to monospace, serif, or sans-serif by argument | `{{typeface typeface=serif}}Text{{/typeface}}`       |
| monospace     | none |                            | Change the typeface to monospace                                   | `{{monospace}}Text{{/monospace}}`                    |
| sans          | none |                            | Change the typeface to sans-serif                                  | `{{sans}}Text{{/sans}}`                              |
| serif         | none |                            | Change the typeface to serif                                       | `{{serif}}Text{{/serif}}`                            |
| subscript     | none |                            | Move text to a subscript                                           | `{{subscript}}Text{{/subscript}}`                    |
| superscript   | none |                            | Move text to a superscript                                         | `{{superscript}}Text{{/superscript}}`                |
| url           | `url=[String]`                    | Make text a clickable link                                         | `{{url url="https://www.example.com/"}}Text{{/url}}` |

### Customize Thistle

**Custom Tags**

Thistle ships with several useful tags out-of-the-box, but for highly stylized text, using all the tags in the format
string can get very tedious and muck up the original intent of the text: which is to do simple decoration of your text
without much fuss.

Do do this, you'll first need to provide Thistle with a custom `ThistleTag` implementation that parses the tag name and
tag attributes from the format string. That Tag must then return the appropriate Android `Span` to apply the formatting
you need.

Custom tag arguments are assumed to always be required. This may be relaxed in a future version, or you can ignore it by
not using `checkArgs` and pulling values from the `args` map manually.

```kotlin
// create a custom implementation of ThistleTag
class CustomStyle : ThistleTag {
    override fun invoke(args: Map<String, Any>): Any {
        // use checkArgs to safely pull properties from the input args and ensure incorrect args are not set 
        return checkArgs(args) {
            val color: Int by int()

            // return anything that can be set to a `SpannableStringBuilder`
            BackgroundColorSpan(color)
        }
    }
}

// create the Thistle parser. It's best to create this once and inject it wherever needed
val thistle = ThistleParser(
    ThistleSyntax.builder {
        from(DefaultAndroidTags)

        // register your custom tab with the Thistle parser
        tag("customStyle") { CustomStyle() }
        // the Link ThistleTag is useful for making portions of text clickable
        tag("inc") { Link { widget: View -> /* do something on link-click */ } }
        tag("dec") { Link { widget: View -> /* do something on link-click */ } }
    }
)

clickMe.applyStyledText(
    thistle,
    "{{inc}}Click Me!{{/inc}}    {{foreground color=@color/red}}|{{/foreground}}    {{dec}}Don't Click Me!{{/dec}}"
)
```

**Custom Value Formats**

Want to create new formats for literals not handled out-of-the-box, or create custom aliases? No problem! You can
provide custom Kudzu parsers for that too. You'll need to use a `MappedParser` to wrap your syntax and format the text
value to the intended literal value.

```kotlin
// create the Thistle parser. It's best to create this once and inject it wherever needed
val thistle = ThistleParser(
    ThistleSyntax.builder {
        from(DefaultAndroidTags)

        literalFormat {
            MappedParser(
                LiteralTokenParser("@color/red")
            ) { Color.RED }
        }

        tag("inc") { Link { widget: View -> /* do something on link-click */ } }
        tag("dec") { Link { widget: View -> /* do something on link-click */ } }
    }
)

clickMe.applyStyledText(
    thistle,
    "{{inc}}Click Me!{{/inc}}    {{foreground color=@color/red}}|{{/foreground}}    {{dec}}Don't Click Me!{{/dec}}"
)
```

**Custom Start/End Tokens**

Thistle allows you full control over the syntax for each tag, also you're also free to create new tags for your needs.
By providing custom Kudzu token parsers for the open or close tags, you can tweak the look to match your preferences.

```kotlin
// create the Thistle parser. It's best to create this once and inject it wherever needed
val thistle = ThistleParser(
    ThistleSyntax.builder {
        from(DefaultAndroidTags)

        customSyntax(
            openTagStartToken = LiteralTokenParser("{%"),
            openTagEndToken = LiteralTokenParser("%}"),
            closeTagStartToken = LiteralTokenParser("{%"),
            closeTagEndToken = LiteralTokenParser("%}"),
        )

        tag("inc") { Link { widget: View -> /* do something on link-click */ } }
        tag("dec") { Link { widget: View -> /* do something on link-click */ } }
    }
)

clickMe.applyStyledText(
    thistle,
    "{% inc %}Click Me!{% inc %}    {% foreground color=#ff0000%}|{% foreground %}    {% dec %}Don't Click Me!{% dec %}"
)
```
