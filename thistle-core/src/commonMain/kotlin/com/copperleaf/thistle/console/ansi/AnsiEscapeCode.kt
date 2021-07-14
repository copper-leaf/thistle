package com.copperleaf.thistle.console.ansi

data class AnsiEscapeCode(
    val code: String
) {
    fun renderCode(): String {
        return "$ansiControlCode$code"
    }

    companion object {
        fun renderResetCode(): String {
            return "$ansiControlCode$ansiReset"
        }

        fun ForegroundColorEscapeCode(
            color: Int,
            bright: Boolean = false
        ): AnsiEscapeCode {
            check(color in ansiForegroundColorsByName.values) { "$color is not a valid ANSI foreground color" }

            return if (bright) {
                AnsiEscapeCode("[$color;1m")
            } else {
                AnsiEscapeCode("[${color}m")
            }
        }

        fun BackgroundColorEscapeCode(
            color: Int,
            bright: Boolean = false
        ): AnsiEscapeCode {
            check(color in ansiBackgroundColorsByName.values) { "$color is not a valid ANSI background color" }

            return if (bright) {
                AnsiEscapeCode("[$color;1m")
            } else {
                AnsiEscapeCode("[${color}m")
            }
        }

        fun StyleEscapeCode(
            style: Int,
        ): AnsiEscapeCode {
            check(style in ansiStylesByName.values) { "$style is not a valid ANSI style" }

            return AnsiEscapeCode("[${style}m")
        }
    }
}
