package com.copperleaf.thistle.console.ansi

import kotlin.test.Test
import kotlin.test.assertEquals

class AnsiStringComponentTest {

    @Test
    fun testBuildSingleComponent() {
        AnsiStringComponent(
            emptyList(),
            "(no escape codes)"
        ).renderComponent()
            .also {
                assertEquals("(no escape codes)", it)
            }

        AnsiStringComponent(
            listOf(
                AnsiEscapeCode.ForegroundColorEscapeCode(FG_GREEN),
            ),
            "(one escape code)"
        ).renderComponent()
            .also {
                assertEquals("\u001B[32m(one escape code)\u001B[0m", it)
            }

        AnsiStringComponent(
            listOf(
                AnsiEscapeCode.ForegroundColorEscapeCode(FG_GREEN),
                AnsiEscapeCode.BackgroundColorEscapeCode(BG_WHITE),
            ),
            "(multiple escape codes)"
        ).renderComponent()
            .also {
                assertEquals("\u001B[32m\u001B[47m(multiple escape codes)\u001B[0m", it)
            }
    }

    @Test
    fun testBuildComponentList() {
        listOf(
            AnsiStringComponent(
                listOf(
                    AnsiEscapeCode.ForegroundColorEscapeCode(FG_GREEN),
                ),
                "(one escape code)"
            ),
            AnsiStringComponent(
                emptyList(),
                "(no escape codes)"
            ),
            AnsiStringComponent(
                listOf(
                    AnsiEscapeCode.ForegroundColorEscapeCode(FG_GREEN),
                    AnsiEscapeCode.BackgroundColorEscapeCode(BG_WHITE),
                ),
                "(multiple escape codes)"
            )
        ).renderToString()
            .also {
                assertEquals(
                    "\u001B[32m(one escape code)\u001B[0m" +
                        "(no escape codes)" +
                        "\u001B[32m\u001B[47m(multiple escape codes)\u001B[0m",
                    it
                )
            }
    }
}
