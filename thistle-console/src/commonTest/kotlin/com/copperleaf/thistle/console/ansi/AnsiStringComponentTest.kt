package com.copperleaf.thistle.console.ansi

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AnsiStringComponentTest : StringSpec({

    "testBuildSingleComponent" {
        AnsiStringComponent(
            emptyList(),
            "(no escape codes)"
        ).renderComponent()
            .also {
                it shouldBe "(no escape codes)"
            }

        AnsiStringComponent(
            listOf(
                AnsiEscapeCode.ForegroundColorEscapeCode(FG_GREEN),
            ),
            "(one escape code)"
        ).renderComponent()
            .also {
                it shouldBe "\u001B[32m(one escape code)\u001B[0m"
            }

        AnsiStringComponent(
            listOf(
                AnsiEscapeCode.ForegroundColorEscapeCode(FG_GREEN),
                AnsiEscapeCode.BackgroundColorEscapeCode(BG_WHITE),
            ),
            "(multiple escape codes)"
        ).renderComponent()
            .also {
                it shouldBe "\u001B[32m\u001B[47m(multiple escape codes)\u001B[0m"
            }
    }

    "testBuildComponentList" {
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
                it shouldBe "\u001B[32m(one escape code)\u001B[0m" +
                    "(no escape codes)" +
                    "\u001B[32m\u001B[47m(multiple escape codes)\u001B[0m"
            }
    }
})
