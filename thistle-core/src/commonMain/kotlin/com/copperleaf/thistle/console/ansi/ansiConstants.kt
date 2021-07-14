package com.copperleaf.thistle.console.ansi

// Common
// ---------------------------------------------------------------------------------------------------------------------

internal const val ansiControlCode = '\u001B'
internal const val ansiReset = "[0m"

// Foreground
// ---------------------------------------------------------------------------------------------------------------------

const val FG_BLACK = 30
const val FG_RED = 31
const val FG_GREEN = 32
const val FG_YELLOW = 33
const val FG_BLUE = 34
const val FG_MAGENTA = 35
const val FG_CYAN = 36
const val FG_WHITE = 37

val ansiForegroundColorsByName
    get() = mapOf(
        "black" to FG_BLACK,
        "red" to FG_RED,
        "green" to FG_GREEN,
        "yellow" to FG_YELLOW,
        "blue" to FG_BLUE,
        "magenta" to FG_MAGENTA,
        "cyan" to FG_CYAN,
        "white" to FG_WHITE,
    )

// Background
// ---------------------------------------------------------------------------------------------------------------------

const val BG_BLACK = 40
const val BG_RED = 41
const val BG_GREEN = 42
const val BG_YELLOW = 43
const val BG_BLUE = 44
const val BG_MAGENTA = 45
const val BG_CYAN = 46
const val BG_WHITE = 47

val ansiBackgroundColorsByName
    get() = mapOf(
        "black" to BG_BLACK,
        "red" to BG_RED,
        "green" to BG_GREEN,
        "yellow" to BG_YELLOW,
        "blue" to BG_BLUE,
        "magenta" to BG_MAGENTA,
        "cyan" to BG_CYAN,
        "white" to BG_WHITE,
    )

// Styles
// ---------------------------------------------------------------------------------------------------------------------

const val STYLE_BOLD = 1
const val STYLE_UNDERLINE = 4
const val STYLE_REVERSE = 7
const val STYLE_STRIKETHROUGH = 9

val ansiStylesByName
    get() = mapOf(
        "bold" to STYLE_BOLD,
        "underline" to STYLE_UNDERLINE,
        "strikethrough" to STYLE_STRIKETHROUGH,
        "reverse" to STYLE_REVERSE,
    )
