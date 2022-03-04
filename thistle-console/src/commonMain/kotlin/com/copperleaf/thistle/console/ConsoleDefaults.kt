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
import com.copperleaf.thistle.console.tags.ConsoleBackgroundColor
import com.copperleaf.thistle.console.tags.ConsoleForegroundColor
import com.copperleaf.thistle.console.tags.ConsoleStyle
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

@ExperimentalStdlibApi
public class ConsoleDefaults : ThistleSyntaxBuilder.Defaults<ConsoleThistleRenderContext, AnsiEscapeCode, String> {

    override fun rendererFactory(): ThistleRendererFactory<ConsoleThistleRenderContext, AnsiEscapeCode, String> {
        return ThistleRendererFactory { ConsoleThistleRenderer(it) }
    }

    /**
     * Adds the default set of Console tags to the [ThistleSyntaxBuilder].
     */
    override fun applyToBuilder(builder: ThistleSyntaxBuilder<ConsoleThistleRenderContext, AnsiEscapeCode, String>) {
        with(builder) {
            tag("background") { ConsoleBackgroundColor() }
            tag("foreground") { ConsoleForegroundColor() }
            tag("black") { ConsoleForegroundColor(FG_BLACK, false) }
            tag("red") { ConsoleForegroundColor(FG_RED, false) }
            tag("green") { ConsoleForegroundColor(FG_GREEN, false) }
            tag("yellow") { ConsoleForegroundColor(FG_YELLOW, false) }
            tag("blue") { ConsoleForegroundColor(FG_BLUE, false) }
            tag("magenta") { ConsoleForegroundColor(FG_MAGENTA, false) }
            tag("cyan") { ConsoleForegroundColor(FG_CYAN, false) }
            tag("white") { ConsoleForegroundColor(FG_WHITE, false) }

            tag("BACKGROUND") { ConsoleBackgroundColor(hardcodedBright = true) }
            tag("FOREGROUND") { ConsoleForegroundColor(hardcodedBright = true) }
            tag("BLACK") { ConsoleForegroundColor(FG_BLACK, hardcodedBright = true) }
            tag("RED") { ConsoleForegroundColor(FG_RED, hardcodedBright = true) }
            tag("GREEN") { ConsoleForegroundColor(FG_GREEN, hardcodedBright = true) }
            tag("YELLOW") { ConsoleForegroundColor(FG_YELLOW, hardcodedBright = true) }
            tag("BLUE") { ConsoleForegroundColor(FG_BLUE, hardcodedBright = true) }
            tag("MAGENTA") { ConsoleForegroundColor(FG_MAGENTA, hardcodedBright = true) }
            tag("CYAN") { ConsoleForegroundColor(FG_CYAN, hardcodedBright = true) }
            tag("WHITE") { ConsoleForegroundColor(FG_WHITE, hardcodedBright = true) }

            tag("style") { ConsoleStyle() }

            tag("strikethrough") { ConsoleStyle(STYLE_STRIKETHROUGH) }
            tag("reverse") { ConsoleStyle(STYLE_REVERSE) }
            tag("b") { ConsoleStyle(STYLE_BOLD) }
            tag("u") { ConsoleStyle(STYLE_UNDERLINE) }
        }
    }
}
