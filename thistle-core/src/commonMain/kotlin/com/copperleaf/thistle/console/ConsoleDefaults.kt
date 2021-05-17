package com.copperleaf.thistle.console

import com.copperleaf.thistle.console.renderer.AnsiEscapeCode
import com.copperleaf.thistle.console.tags.BackgroundColor
import com.copperleaf.thistle.console.tags.ForegroundColor
import com.copperleaf.thistle.console.tags.Style
import com.copperleaf.thistle.console.tags.Style.Companion.CONSOLE_BOLD
import com.copperleaf.thistle.console.tags.Style.Companion.CONSOLE_REVERSE
import com.copperleaf.thistle.console.tags.Style.Companion.CONSOLE_STRIKETHROUGH
import com.copperleaf.thistle.console.tags.Style.Companion.CONSOLE_UNDERLINE
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

@ExperimentalStdlibApi
object ConsoleDefaults : ThistleSyntaxBuilder.Defaults<AnsiEscapeCode> {

    /**
     * Adds the default set of Console tags to the [ThistleSyntaxBuilder].
     */
    override fun apply(builder: ThistleSyntaxBuilder<AnsiEscapeCode>) {
        with(builder) {
            tag("background") { BackgroundColor() }
            tag("foreground") { ForegroundColor() }
            tag("black") { ForegroundColor(ForegroundColor.BLACK, false) }
            tag("red") { ForegroundColor(ForegroundColor.RED, false) }
            tag("green") { ForegroundColor(ForegroundColor.GREEN, false) }
            tag("yellow") { ForegroundColor(ForegroundColor.YELLOW, false) }
            tag("blue") { ForegroundColor(ForegroundColor.BLUE, false) }
            tag("magenta") { ForegroundColor(ForegroundColor.MAGENTA, false) }
            tag("cyan") { ForegroundColor(ForegroundColor.CYAN, false) }
            tag("white") { ForegroundColor(ForegroundColor.WHITE, false) }

            tag("BACKGROUND") { BackgroundColor(hardcodedBright = true) }
            tag("FOREGROUND") { ForegroundColor(hardcodedBright = true) }
            tag("BLACK") { ForegroundColor(ForegroundColor.BLACK, hardcodedBright = true) }
            tag("RED") { ForegroundColor(ForegroundColor.RED, hardcodedBright = true) }
            tag("GREEN") { ForegroundColor(ForegroundColor.GREEN, hardcodedBright = true) }
            tag("YELLOW") { ForegroundColor(ForegroundColor.YELLOW, hardcodedBright = true) }
            tag("BLUE") { ForegroundColor(ForegroundColor.BLUE, hardcodedBright = true) }
            tag("MAGENTA") { ForegroundColor(ForegroundColor.MAGENTA, hardcodedBright = true) }
            tag("CYAN") { ForegroundColor(ForegroundColor.CYAN, hardcodedBright = true) }
            tag("WHITE") { ForegroundColor(ForegroundColor.WHITE, hardcodedBright = true) }

            tag("style") { Style() }

            tag("strikethrough") { Style(CONSOLE_STRIKETHROUGH) }
            tag("reverse") { Style(CONSOLE_REVERSE) }
            tag("b") { Style(CONSOLE_BOLD) }
            tag("u") { Style(CONSOLE_UNDERLINE) }
        }
    }
}
