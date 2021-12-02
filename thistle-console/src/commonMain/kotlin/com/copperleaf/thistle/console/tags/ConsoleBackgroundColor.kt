package com.copperleaf.thistle.console.tags

import com.copperleaf.thistle.console.ansi.AnsiEscapeCode
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode.Companion.BackgroundColorEscapeCode
import com.copperleaf.thistle.console.ansi.ansiBackgroundColorsByName
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.boolean
import com.copperleaf.thistle.core.renderer.enum

class ConsoleBackgroundColor(
    private val hardcodedColor: Int? = null,
    private val hardcodedBright: Boolean? = null,
) : ThistleTagFactory<ConsoleThistleRenderContext, AnsiEscapeCode> {
    override fun invoke(renderContext: ConsoleThistleRenderContext): AnsiEscapeCode {
        return checkArgs(renderContext) {
            val color: Int by enum(hardcodedColor) { ansiBackgroundColorsByName }
            val bright: Boolean by boolean(hardcodedBright)

            BackgroundColorEscapeCode(color, bright)
        }
    }
}
