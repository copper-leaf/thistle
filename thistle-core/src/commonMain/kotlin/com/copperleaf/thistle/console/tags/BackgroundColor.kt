package com.copperleaf.thistle.console.tags

import com.copperleaf.thistle.console.renderer.AnsiEscapeCode
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class BackgroundColor(
    private val hardcodedColor: Int? = null,
    private val hardcodedBright: Boolean? = null,
) : ThistleTag<AnsiEscapeCode> {
    companion object {
        const val BLACK = 40
        const val RED = 41
        const val GREEN = 42
        const val YELLOW = 43
        const val BLUE = 44
        const val MAGENTA = 45
        const val CYAN = 46
        const val WHITE = 47
    }

    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): AnsiEscapeCode {
        return checkArgs(args) {
            val color: Int by enum(hardcodedColor) {
                mapOf(
                    "black" to BackgroundColor.BLACK,
                    "red" to BackgroundColor.RED,
                    "green" to BackgroundColor.GREEN,
                    "yellow" to BackgroundColor.YELLOW,
                    "blue" to BackgroundColor.BLUE,
                    "magenta" to BackgroundColor.MAGENTA,
                    "cyan" to BackgroundColor.CYAN,
                    "white" to BackgroundColor.WHITE,
                )
            }
            val bright: Boolean by boolean(hardcodedBright)

            if (bright) {
                AnsiEscapeCode("[$color;1m")
            } else {
                AnsiEscapeCode("[${color}m")
            }
        }
    }
}
