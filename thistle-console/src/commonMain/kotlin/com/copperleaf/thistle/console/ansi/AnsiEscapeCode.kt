package com.copperleaf.thistle.console.ansi

public data class AnsiEscapeCode(
    val code: String
) {
    public fun renderCode(): String {
        return "$ansiControlCode$code"
    }

    public companion object {
        public fun renderResetCode(): String {
            return "$ansiControlCode$ansiReset"
        }

        public fun ForegroundColorEscapeCode(
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

        public fun BackgroundColorEscapeCode(
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

        public fun StyleEscapeCode(
            style: Int,
        ): AnsiEscapeCode {
            check(style in ansiStylesByName.values) { "$style is not a valid ANSI style" }

            return AnsiEscapeCode("[${style}m")
        }
    }
}
