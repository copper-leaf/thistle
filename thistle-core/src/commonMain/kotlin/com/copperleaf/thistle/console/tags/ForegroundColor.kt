package com.copperleaf.thistle.console.tags

import com.copperleaf.thistle.console.ansi.ansiForegroundColorsByName
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode.Companion.ForegroundColorEscapeCode
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class ForegroundColor(
    private val hardcodedColor: Int? = null,
    private val hardcodedBright: Boolean? = null,
) : ThistleTag<ConsoleThistleRenderContext, AnsiEscapeCode> {
    override fun invoke(renderContext: ConsoleThistleRenderContext): AnsiEscapeCode {
        return checkArgs(renderContext) {
            val color: Int by enum(hardcodedColor) { ansiForegroundColorsByName }
            val bright: Boolean by boolean(hardcodedBright)

            ForegroundColorEscapeCode(color, bright)
        }
    }
}
