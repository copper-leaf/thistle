package com.copperleaf.thistle.console.tags

import com.copperleaf.thistle.console.renderer.AnsiEscapeCode
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class ForegroundColor(
    private val hardcodedColor: Int? = null,
    private val hardcodedBright: Boolean? = null,
) : ThistleTag<AnsiEscapeCode> {
    companion object {
        const val BLACK = 30
        const val RED = 31
        const val GREEN = 32
        const val YELLOW = 33
        const val BLUE = 34
        const val MAGENTA = 35
        const val CYAN = 36
        const val WHITE = 37
    }

    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): AnsiEscapeCode {
        return checkArgs(args) {
            val color: Int by enum(hardcodedColor) {
                mapOf(
                    "black" to ForegroundColor.BLACK,
                    "red" to ForegroundColor.RED,
                    "green" to ForegroundColor.GREEN,
                    "yellow" to ForegroundColor.YELLOW,
                    "blue" to ForegroundColor.BLUE,
                    "magenta" to ForegroundColor.MAGENTA,
                    "cyan" to ForegroundColor.CYAN,
                    "white" to ForegroundColor.WHITE,
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
