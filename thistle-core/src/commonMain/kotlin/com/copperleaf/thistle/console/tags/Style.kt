package com.copperleaf.thistle.console.tags

import com.copperleaf.thistle.console.renderer.AnsiEscapeCode
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class Style(
    private val hardcodedStyle: Int? = null
) : ThistleTag<AnsiEscapeCode> {
    companion object {
        const val CONSOLE_BOLD = 1
        const val CONSOLE_UNDERLINE = 4
        const val CONSOLE_REVERSE = 7
        const val CONSOLE_STRIKETHROUGH = 9
    }

    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): AnsiEscapeCode {
        return checkArgs(args) {
            val style: Int by enum(hardcodedStyle) {
                mapOf(
                    "bold" to CONSOLE_BOLD,
                    "underline" to CONSOLE_UNDERLINE,
                    "strikethrough" to CONSOLE_STRIKETHROUGH,
                    "reverse" to CONSOLE_REVERSE,
                )
            }

            AnsiEscapeCode("[${style}m")
        }
    }
}
