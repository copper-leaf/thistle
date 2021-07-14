package com.copperleaf.thistle.console.ansi

import kotlin.test.Test
import kotlin.test.assertEquals

class AnsiTreeBuilderTest {

    @Test
    fun testBuildSingleComponent() {
        var text1 = ""
        var text2 = ""
        var text3 = ""

        buildAnsiString {
            appendChild({ text1 = it; AnsiEscapeCode.ForegroundColorEscapeCode(FG_GREEN) }) {
                appendText("(one escape code)")
            }
            appendText("(no escape codes)")
            appendChild({ text2 = it; AnsiEscapeCode.ForegroundColorEscapeCode(FG_GREEN) }) {
                appendText("(before)")
                appendChild({ text3 = it; AnsiEscapeCode.BackgroundColorEscapeCode(BG_WHITE) }) {
                    appendText("(multiple escape codes)")
                }
                appendText("(after)")
            }
        }.flatten()
            .renderToString()
            .also {
                assertEquals(
                    "\u001B[32m(one escape code)\u001B[0m" +
                        "(no escape codes)" +
                        "\u001B[32m(before)\u001B[0m" +
                        "\u001B[32m\u001B[47m(multiple escape codes)\u001B[0m" +
                        "\u001B[32m(after)\u001B[0m",
                    it
                )

                assertEquals("(one escape code)", text1)
                assertEquals("(before)(multiple escape codes)(after)", text2)
                assertEquals("(multiple escape codes)", text3)
            }
    }
}
