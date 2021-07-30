package com.copperleaf.thistle.console

import com.copperleaf.thistle.console.ansi.AnsiEscapeCode
import com.copperleaf.thistle.console.ansi.FG_BLACK
import com.copperleaf.thistle.console.ansi.FG_BLUE
import com.copperleaf.thistle.console.ansi.FG_CYAN
import com.copperleaf.thistle.console.ansi.FG_GREEN
import com.copperleaf.thistle.console.ansi.FG_MAGENTA
import com.copperleaf.thistle.console.ansi.FG_RED
import com.copperleaf.thistle.console.ansi.FG_WHITE
import com.copperleaf.thistle.console.ansi.FG_YELLOW
import com.copperleaf.thistle.console.ansi.STYLE_BOLD
import com.copperleaf.thistle.console.ansi.STYLE_REVERSE
import com.copperleaf.thistle.console.ansi.STYLE_STRIKETHROUGH
import com.copperleaf.thistle.console.ansi.STYLE_UNDERLINE
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderContext
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderer
import com.copperleaf.thistle.console.tags.BackgroundColor
import com.copperleaf.thistle.console.tags.ForegroundColor
import com.copperleaf.thistle.console.tags.Style
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

@ExperimentalStdlibApi
object ConsoleDefaults : ThistleSyntaxBuilder.Defaults<ConsoleThistleRenderContext, AnsiEscapeCode, String> {

    override fun rendererFactory(): ThistleRendererFactory<ConsoleThistleRenderContext, AnsiEscapeCode, String> {
        return ThistleRendererFactory { ConsoleThistleRenderer(it) }
    }

    /**
     * Adds the default set of Console tags to the [ThistleSyntaxBuilder].
     */
    override fun applyToBuilder(builder: ThistleSyntaxBuilder<ConsoleThistleRenderContext, AnsiEscapeCode, String>) {
        with(builder) {
            tag("background") { BackgroundColor() }
            tag("foreground") { ForegroundColor() }
            tag("black") { ForegroundColor(FG_BLACK, false) }
            tag("red") { ForegroundColor(FG_RED, false) }
            tag("green") { ForegroundColor(FG_GREEN, false) }
            tag("yellow") { ForegroundColor(FG_YELLOW, false) }
            tag("blue") { ForegroundColor(FG_BLUE, false) }
            tag("magenta") { ForegroundColor(FG_MAGENTA, false) }
            tag("cyan") { ForegroundColor(FG_CYAN, false) }
            tag("white") { ForegroundColor(FG_WHITE, false) }

            tag("BACKGROUND") { BackgroundColor(hardcodedBright = true) }
            tag("FOREGROUND") { ForegroundColor(hardcodedBright = true) }
            tag("BLACK") { ForegroundColor(FG_BLACK, hardcodedBright = true) }
            tag("RED") { ForegroundColor(FG_RED, hardcodedBright = true) }
            tag("GREEN") { ForegroundColor(FG_GREEN, hardcodedBright = true) }
            tag("YELLOW") { ForegroundColor(FG_YELLOW, hardcodedBright = true) }
            tag("BLUE") { ForegroundColor(FG_BLUE, hardcodedBright = true) }
            tag("MAGENTA") { ForegroundColor(FG_MAGENTA, hardcodedBright = true) }
            tag("CYAN") { ForegroundColor(FG_CYAN, hardcodedBright = true) }
            tag("WHITE") { ForegroundColor(FG_WHITE, hardcodedBright = true) }

            tag("style") { Style() }

            tag("strikethrough") { Style(STYLE_STRIKETHROUGH) }
            tag("reverse") { Style(STYLE_REVERSE) }
            tag("b") { Style(STYLE_BOLD) }
            tag("u") { Style(STYLE_UNDERLINE) }
        }
    }
}
