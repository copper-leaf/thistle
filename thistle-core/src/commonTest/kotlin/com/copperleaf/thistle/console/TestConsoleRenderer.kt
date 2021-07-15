package com.copperleaf.thistle.console

import com.copperleaf.kudzu.parser.ParserException
import com.copperleaf.thistle.core.parser.ThistleParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalStdlibApi::class)
/* ktlint-disable max-line-length */
class TestConsoleRenderer {

    @Test
    fun consoleRendererTests() {
        val thistle = ThistleParser(ConsoleDefaults)

        listOf(
            "begin {{foreground color=red bright=false}}foreground{{/foreground}} end",
            "begin {{background color=green bright=false}}background{{/background}} end",
            "begin {{foreground color=red bright=false}}{{background color=green bright=false}}foreground and background{{/background}}{{/foreground}} end",
            "{{reverse}}begin {{foreground color=red bright=false}}foreground{{/foreground}} end{{/reverse}}",
            "{{reverse}}begin {{background color=green bright=false}}background{{/background}} end{{/reverse}}",
            "{{reverse}}begin {{foreground color=red bright=false}}{{background color=green bright=false}}foreground and background{{/background}}{{/foreground}} end{{/reverse}}",

            "begin {{black}}normal black foreground{{/black}} {{BLACK}}bright black foreground{{/BLACK}} end",
            "begin {{red}}normal red foreground{{/red}} {{RED}}bright red foreground{{/RED}} end",
            "begin {{green}}normal green foreground{{/green}} {{GREEN}}bright green foreground{{/GREEN}} end",
            "begin {{yellow}}normal yellow foreground{{/yellow}} {{YELLOW}}bright yellow foreground{{/YELLOW}} end",
            "begin {{blue}}normal blue foreground{{/blue}} {{BLUE}}bright blue foreground{{/BLUE}} end",
            "begin {{magenta}}normal magenta foreground{{/magenta}} {{MAGENTA}}bright magenta foreground{{/MAGENTA}} end",
            "begin {{cyan}}normal cyan foreground{{/cyan}} {{CYAN}}bright cyan foreground{{/CYAN}} end",
            "begin {{white}}normal white foreground{{/white}} {{WHITE}}bright white foreground{{/WHITE}} end",

            "begin {{b}}bold{{/b}} end",
            "begin {{u}}underline{{/u}} end",
            "begin {{reverse}}reverse{{/reverse}} end",
            "begin {{strikethrough}}reverse{{/strikethrough}} end",

            "begin {{style style=bold}}bold{{/style}} end",
            "begin {{style style=underline}}underline{{/style}} end",
            "begin {{style style=reverse}}reverse{{/style}} end",
            "begin {{style style=strikethrough}}reverse{{/style}} end",
            """
            |start:all
            |{{reverse}}
            |    start:reverse
            |    {{red}}
            |        start:red
            |        end:red
            |    {{/red}}
            |    end:reverse
            |{{/reverse}}
            |{{background color=black bright=true}}
            |    start:background
            |    {{CYAN}}
            |        start:blue
            |        {{u}}
            |            start:underline
            |            end:underline
            |        {{/u}}
            |        end:blue
            |    {{/CYAN}}
            |    end:background
            |{{/background}}
            |end:all
            """.trimMargin().replace('\n', ' ').replace("\\s+".toRegex(), " "),
        ).forEach {
            printlnStyledText(thistle, it)
        }
    }

    @Test
    fun testMismatchedEndTag() {
        val thistle = ThistleParser(ConsoleDefaults)
        assertFailsWith<ParserException> {
            printlnStyledText(thistle, "begin {{black}}normal black foreground{{/blue}}")
        }.also {
            assertEquals(
                "Parse error: Mismatched closing tag: Expected tag name to be 'black', got 'blue' " +
                    "(SimpleTagParser at 1:7)",
                it.message
            )
        }
    }

    @Test
    fun testUnknownTagName() {
        val thistle = ThistleParser(ConsoleDefaults)
        assertFailsWith<IllegalStateException> {
            printlnStyledText(thistle, "begin {{orange}}normal orange foreground{{/orange}}")
        }.also {
            assertEquals(
                "Unknown tag: orange. Valid tag names: [background, foreground, black, red, green, yellow, blue, " +
                    "magenta, cyan, white, BACKGROUND, FOREGROUND, BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, " +
                    "WHITE, style, strikethrough, reverse, b, u]",
                it.message
            )
        }
    }
}
