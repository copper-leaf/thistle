---
---

# Thistle

Kotlin multiplatform String markup library, inspired by [SRML](https://github.com/jasonwyatt/SRML).

![GitHub release (latest by date)](https://img.shields.io/github/v/release/copper-leaf/thistle)
![Maven Central](https://img.shields.io/maven-central/v/io.github.copper-leaf/thistle-core)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.6.10-orange)

## Overview

Thistle is a library for converting Strings with markup tags into text with inline styles. It 
supports a variety of different targets, including Android, Compose UI, and Consoles, but with a 
common markup syntax shared by all targets.

**Example Usage**

```kotlin
// Android
{% snippet 'android-preview' %}

// Compose (both Android and Desktop)
{% snippet 'compose-preview' %}

// Console (ANSI codes)
{% snippet 'console-preview' %}
```

**Motivation**

All UI platforms have some concept of inline text styling (using a single "text view" but having 
portions of the text with different colors, fonts, font weight, etc.). However, the API for actually
using these inline styles is usually very verbose and difficult to understand, and the actual
implementation of these styles varies greatly by platform. Some use a tree-like structure which is
relatively easy to work with (HTML `<span>`, or KTX extensions for Android `Spannable`), while 
others have you manually computing text offsets and attaching markup tags manually (Compose, iOS). 
And don't even get me started on Console ANSI codes...

Thistle aims to abstract all that complexity away, so that you don't need to know anything about the
platform's underlying text-styling mechanisms. You just provide your Strings and mark them up with
the intended styling, and Thistle will take care of the rest for you. The syntax is similar to many
other markup languages, such as Twig or Handlebars, but can also be tweaked if needed.

The initial idea came from an old Android library, [SRML](https://github.com/jasonwyatt/SRML). It's 
a small library that has been a lifesaver for my team for years, and Thistle began as a 
re-implementation of that library in Kotlin using a proper parser and AST instead of Regex. Having
this intermediate AST representation allows Thistle to easily adapt the same syntax to the different 
APIs needed for each target.

_Important Note: Thistle is not designed to be a full template engine. The parser is not optimized
for handling large inputs, and the syntax is intentionally limited to only work with styling and 
simple interpolation, but no logic. This is to keep the library focused and avoid bloat, while also 
maximizing the ability for sharing input Strings and internal code across platforms without major 
compatibility issues._

## Installation

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

## Syntax

The [sample apps](https://github.com/copper-leaf/thistle/tree/main/examples) demo a variety of 
different tags, use-cases, and customizations, but here's a rundown of the basic syntax (which can 
be [tweaked](#customization) to your needs).

The following examples all demonstrate usage on Android.

### Tags

Tags work similar to inline HTML tags. They can be interspersed throughout the main text, and they can also be nested
within each other arbitrarily deep.

The following example shows usage of both the `foreground` and `background` tags placed inside normal text:

{% verbatim %}
```
This text will be {{foreground color=#FF0000}}red{{/foreground}}, while this one has a {{background color=#0000FF}}blue{{/background}} background.
```
{% endverbatim %}

![syntax_tags]({{ 'assets/media/syntax_tags.png'|asset }})

### Tag Parameters

Also like HTML tags, Thistle tags may be parameterized to tweak their rendering behavior. Parameters are assumed to be
required (though this may be relaxed in the future), and they are strongly-typed. Values of a different type will not be
coerced, and will instead just throw an exception.

Tag parameters are a list of `key=value` pairs separated by whitespace. Unlike HTML parameters, however, Thistle
parameters should not be wrapped in quotes (`"`) unless the value is actually intended to be a String.

The `foreground` tag requires a `color` parameter to be set, which must be a hex color literal:

{% verbatim %}
```
{{foreground color=#FF0000}}red{{/foreground}}
```
{% endverbatim %}

![syntax_parameters]({{ 'assets/media/syntax_parameters.png'|asset }})

Thistle recognizes the following formats for parameter values. You are also able to
[provide your own value formats](#custom-value-formats) for aliasing or loading values from another source.

| Type            | Example              | Notes                                                                          |
| --------------- | -------------------- | ------------------------------------------------------------------------------ |
| Boolean         | `true`, `false`      |                                                                                |
| Double          | `1.1`                |                                                                                |
| Int             | `1`                  |                                                                                |
| String          | `"This is a string"` | String in quotes may contain spaces and escaped Unicode sequences              |
| Char            | `'c'`                |                                                                                |
| Hex Color       | `#FF0000`            | Parsed to an Int with `FF` alpha channel                                       |
| Context Value   | `context.username`   | See "context data" below                                                       |
| Unquoted String | `monospace`          | Unquoted string must be a single ASCII word with no spaces or Unicode sequences|
{.table}

### Context Data

Thistle renders each format string with an optional "context data" map. The values in this map can be accessed as tag
parameters, and they can also be interpolated into the output as dynamic text.

For example, given the following context data map:

```kotlin
val contextData = mapOf(
    "themeRed" to Color.RED
)
```

You can reference `themeRed` from the foreground tag parameters:

{% verbatim %}
```
{{foreground color=context.themeRed}}red{{/foreground}}
```
{% endverbatim %}

![syntax_context_data]({{ 'assets/media/syntax_context_data.png'|asset }})

### Interpolation

Another useful feature of the context data is to render dynamic text into the output. This may be at the top-level, or
within a tag's content. All objects in the context map are converted to text with `.toString()`, and Thistle does not
support any further formatting or transformation on the interpolated values from the format string. Thus, if you need to
customize the output of a variable, you will need to convert it manually to a string before adding it to the context data map.

For example, given the following context data map:

```kotlin
val contextData = mapOf(
    "username" to "AliceBob123",
    "userId" to "123456789",
)
```

`username` and `userId` can be rendered into the output dynamically:

{% verbatim %}
```
Account: {{b}} {username} {{/b}} ({userId})
```
{% endverbatim %}

![syntax_interpolation]({{ 'assets/media/syntax_interpolation.png'|asset }})

## Targets

Thistle uses a common parser, and a variety of renderers to format text at runtime. It supports both inline-styling, and 
basic interpolation. Currently, only the Android renderer is built, but more targets are planned.

Thistle also parses inputs to an Abstract Syntax Tree (AST) that can be cached and rendered multiple times for 
performance. For small inputs or only rendering once, this may not be necessary, but parsing is significantly slower 
than rendering (on the order of 10s of milliseconds for parsing, vs microseconds for rendering), so caching the AST
can be quite useful for text that gets re-rendered many times per second.

Both the parser and the resulting AST are fully immutable and thus are completely thread-safe.

Each target will naturally have different rendering capabilities, which are documented in the sections below.

### Android

On Android, Thistle parses a String into a `Spanned` instance that can be set to a TextView. The Thistle format replaces
"tags" with Android `Span`s, wrapping the appropriate text. The normal Span API can be a bit of a pain, and Thistle
makes this simpler, and also allows you to change the span formatting at runtime rather than compile time.

```kotlin
{% snippet 'android-basic-usage' %}
```

The Android target also adds an additional value format for accessing application resources, using the
same `@` syntax used in XML layouts.

| Type              | Example                  | Notes  |
| ----------------- | ------------------------ | ------ |
| String resource   | `@string/app_name`       |        |
| Color resource    | `@color/colorPrimary`    |        |
| Drawable resource | `@drawable/ic_launcher`  |        |
{.table}

![sample_android_app]({{ 'assets/media/sample_android.gif'|asset }})

**Default Tags**

{% verbatim %}

| Tag Name      | Params                            | Description                                                        | Example |
| ------------- | --------------------------------- | ------------------------------------------------------------------ | ------- |
| foreground    | `color=[hex color]`               | Change text color                                                  | `{{foreground color=#FFFF00}}Text{{/foreground}}`    |
| background    | `color=[hex color]`               | Change background color                                            | `{{background color=#FFFF00}}Text{{/background}}`    |
| style         | `style=[bold,italic]`             | Set text to bold or italic by argument                             | `{{style style=bold}}Text{{/style}}`                 |
| b             | none                              | Set text style to bold                                             | `{{b}}Text{{/b}}`                                    |
| i             | none                              | Set text style to italic                                           | `{{i}}Text{{/i}}`                                    |
| u             | none                              | Add underline to text                                              | `{{u}}Text{{/u}}`                                    |
| strikethrough | none                              | Add strikethrough to text                                          | `{{strikethrough}}Text{{/strikethrough}}`            |
| typeface      | `typeface=[monospace,sans,serif]` | Change the typeface to monospace, serif, or sans-serif by argument | `{{typeface typeface=serif}}Text{{/typeface}}`       |
| monospace     | none                              | Change the typeface to monospace                                   | `{{monospace}}Text{{/monospace}}`                    |
| sans          | none                              | Change the typeface to sans-serif                                  | `{{sans}}Text{{/sans}}`                              |
| serif         | none                              | Change the typeface to serif                                       | `{{serif}}Text{{/serif}}`                            |
| subscript     | none                              | Move text to a subscript                                           | `{{subscript}}Text{{/subscript}}`                    |
| superscript   | none                              | Move text to a superscript                                         | `{{superscript}}Text{{/superscript}}`                |
| icon           | `drawable=[@drawable/]`          | Replace text with an inline icon drawable                          | `{{icon drawable=@drawable/icon}}Text{{/icon}}`      |
| url           | `url=[String]`                    | Make text a clickable link                                         | `{{url url="https://www.example.com/"}}Text{{/url}}` |
{.table}

{% endverbatim %}

- [Android Example project](https://github.com/copper-leaf/thistle/tree/main/examples/android)

### Compose UI

Thistle supporting rendering to Compose UI by building an `AnnotatedString`, which supports both 
Android and Desktop. The set of available tags for Compose are the same as Android (except it is 
missing `icon` and `url`), allowing one to freely share Thistle strings between the standard and 
Compose Android renderers, if needed.

```kotlin
{% snippet 'compose-basic-usage' %}
```

![sample_android_app]({{ 'assets/media/sample_compose.png'|asset }})

**Default Tags**

{% verbatim %}

| Tag Name      | Params                            | Description                                                        | Example |
| ------------- | --------------------------------- | ------------------------------------------------------------------ | ------- |
| foreground    | `color=[hex color]`               | Change text color                                                  | `{{foreground color=#FFFF00}}Text{{/foreground}}`    |
| background    | `color=[hex color]`               | Change background color                                            | `{{background color=#FFFF00}}Text{{/background}}`    |
| style         | `style=[bold,italic]`             | Set text to bold or italic by argument                             | `{{style style=bold}}Text{{/style}}`                 |
| b             | none                              | Set text style to bold                                             | `{{b}}Text{{/b}}`                                    |
| i             | none                              | Set text style to italic                                           | `{{i}}Text{{/i}}`                                    |
| u             | none                              | Add underline to text                                              | `{{u}}Text{{/u}}`                                    |
| strikethrough | none                              | Add strikethrough to text                                          | `{{strikethrough}}Text{{/strikethrough}}`            |
| typeface      | `typeface=[monospace,sans,serif]` | Change the typeface to monospace, serif, or sans-serif by argument | `{{typeface typeface=serif}}Text{{/typeface}}`       |
| monospace     | none                              | Change the typeface to monospace                                   | `{{monospace}}Text{{/monospace}}`                    |
| sans          | none                              | Change the typeface to sans-serif                                  | `{{sans}}Text{{/sans}}`                              |
| serif         | none                              | Change the typeface to serif                                       | `{{serif}}Text{{/serif}}`                            |
| subscript     | none                              | Move text to a subscript                                           | `{{subscript}}Text{{/subscript}}`                    |
| superscript   | none                              | Move text to a superscript                                         | `{{superscript}}Text{{/superscript}}`                |
{.table}

{% endverbatim %}

- [Compose Android Example project](https://github.com/copper-leaf/thistle/tree/main/examples/compose-android)
- [Compose Desktop Example project](https://github.com/copper-leaf/thistle/tree/main/examples/compose-desktop)

### Console

For rendering to a console, Thistle converts the normal markup tags into 
[ANSI escape codes](https://en.wikipedia.org/wiki/ANSI_escape_code). It currently supports 16-bit colors for both 
foreground and background, and some other basic styling options.

```kotlin
{% snippet 'console-basic-usage' %}
```

![sample_console]({{ 'assets/media/sample_console.png'|asset }})

**Default Tags**

Color tags support both normal and "bright" or "bold" colors. Typically, a tag name that 
is all uppercase letters will render the color in "bold", while all lowercase letters will either render in normal style
or offer a parameter for manually configuring it.

Unless otherwise specified, in the table below `ansi color` will refer to one of the following color values: `black`, 
`red`, `green`, `yellow`, `blue`, `magenta`, `cyan`, `white`.

By default, when an ANSI "reset" code is encountered, all styling is reset. Thistle automatically handles re-applying
nested tags after a reset, so using nested tags works exactly as you would expect them to.

{% verbatim %}

| Tag Name              | Params                                         | Description                                                        | Example                                                 |
| --------------------- | ---------------------------------------------- | ------------------------------------------------------------------ | ------------------------------------------------------- |
| foreground/FOREGROUND | `color=[ansi color]`, `bold=[true,false]`      | Change text color                                                  | `{{foreground color=red bold=true}}Text{{/foreground}}` |
| background/FOREGROUND | `color=[ansi color]`, `bold=[true,false]`      | Change background color                                            | `{{background color=red bold=true}}Text{{/background}}` |
| style                 | `style=[bold,underline,strikethrough,reverse]` | Set text to bold, underline, strikethrough, or reverse by argument | `{{style style=bold}}Text{{/style}}`                    |
| black/BLACK           | none                                           | Set foreground color to black                                      | `{{black}}Text{{/black}}`                               |
| red/RED               | none                                           | Set foreground color to red                                        | `{{red}}Text{{/red}}`                                   |
| green/GREEN           | none                                           | Set foreground color to green                                      | `{{green}}Text{{/green}}`                               |
| yellow/YELLOW         | none                                           | Set foreground color to yellow                                     | `{{yellow}}Text{{/yellow}}`                             |
| blue/BLUE             | none                                           | Set foreground color to blue                                       | `{{blue}}Text{{/blue}}`                                 |
| magenta/MAGENTA       | none                                           | Set foreground color to magenta                                    | `{{magenta}}Text{{/magenta}}`                           |
| cyan/CYAN             | none                                           | Set foreground color to cyan                                       | `{{cyan}}Text{{/cyan}}`                                 |
| white/WHITE           | none                                           | Set foreground color to white                                      | `{{white}}Text{{/white}}`                               |
| b                     | none                                           | Set text style to bold                                             | `{{b}}Text{{/b}}`                                       |
| u                     | none                                           | Add underline to text                                              | `{{u}}Text{{/u}}`                                       |
| reverse               | none                                           | Add underline to text                                              | `{{reverse}}Text{{/reverse}}`                           |
| strikethrough         | none                                           | Add strikethrough to text                                          | `{{strikethrough}}Text{{/strikethrough}}`               |
{.table}

{% endverbatim %}

### iOS

TODO (follow issue [here](https://github.com/copper-leaf/thistle/issues/2))

### JS DOM

TODO (follow issue [here](https://github.com/copper-leaf/thistle/issues/3))

## Customization

All targets use the same Parser implementation, and only differ in the available tags and value formats. The following
customizations are available for all platforms.

### Custom Tags

Thistle ships with several useful tags out-of-the-box, but for highly stylized text, using all the tags in the format
string can get very tedious and muck up the original intent of the text: which is to do simple decoration of your text
without much fuss.

Do do this, you'll first need to provide Thistle with a custom `ThistleTag` implementation that parses the tag name and
tag attributes from the format string. That Tag must then return the appropriate Android `Span` to apply the formatting
you need.

Custom tag arguments are assumed to always be required. This may be relaxed in a future version, or you can ignore it by
not using `checkArgs` and pulling values from the `args` map manually.

```kotlin
{% snippet 'android-custom-tags' %}
```

### Custom Value Formats

Want to create new formats for literals not handled out-of-the-box, or create custom aliases? No problem! You can
provide custom Kudzu parsers for that too. You'll need to use a `MappedParser` to wrap your syntax and format the text
value to the intended literal value.

```kotlin
{% snippet 'android-custom-value-formats' %}
```

### Custom Start/End Tokens

Thistle allows you full control over the syntax for each tag, also you're also free to create new tags for your needs.
By providing custom Kudzu token parsers for the open or close tags, you can tweak the look to match your preferences.

```kotlin
{% snippet 'android-custom-start-end-tokens' %}
```
