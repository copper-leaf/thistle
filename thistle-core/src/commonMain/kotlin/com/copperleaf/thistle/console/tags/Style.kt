package com.copperleaf.thistle.console.tags

import com.copperleaf.thistle.console.ansi.ansiStylesByName
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode.Companion.StyleEscapeCode
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class Style(
    private val hardcodedStyle: Int? = null
) : ThistleTag<ConsoleThistleRenderContext, AnsiEscapeCode> {
    override fun invoke(renderContext: ConsoleThistleRenderContext): AnsiEscapeCode {
        return checkArgs(renderContext) {
            val style: Int by enum(hardcodedStyle) { ansiStylesByName }

            StyleEscapeCode(style)
        }
    }
}
