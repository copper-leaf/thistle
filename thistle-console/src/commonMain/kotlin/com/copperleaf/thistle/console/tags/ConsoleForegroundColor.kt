package com.copperleaf.thistle.console.tags

import com.copperleaf.thistle.console.ansi.AnsiEscapeCode
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode.Companion.ForegroundColorEscapeCode
import com.copperleaf.thistle.console.ansi.ansiForegroundColorsByName
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.boolean
import com.copperleaf.thistle.core.renderer.enum

public class ConsoleForegroundColor(
    private val hardcodedColor: Int? = null,
    private val hardcodedBright: Boolean? = null,
) : ThistleTagFactory<ConsoleThistleRenderContext, AnsiEscapeCode> {
    override fun invoke(renderContext: ConsoleThistleRenderContext): AnsiEscapeCode {
        return checkArgs(renderContext) {
            val color: Int by enum(hardcodedColor) { ansiForegroundColorsByName }
            val bright: Boolean by boolean(hardcodedBright)

            ForegroundColorEscapeCode(color, bright)
        }
    }
}
