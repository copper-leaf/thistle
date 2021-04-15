package com.copperleaf.thistle.parser

import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.expectThat
import com.copperleaf.thistle.parsedCorrectly
import kotlin.test.Test

@ExperimentalStdlibApi
class TestParser {

    @Test
    fun test1() {
        val tag1 = ThistleTag { }
        val tag2 = ThistleTag { }

        val thistle = ThistleParser(
            ThistleSyntax.builder {
                tag("one") { tag1 }
                tag("two") { tag2 }
            }
        )

        "this is {{one}}tag 1{{/one}}".let { input ->
            val result = thistle.parser.parse(ParserContext.fromString(input))
            expectThat(result)
                .parsedCorrectly(
                    """
                    |(ManyNode:
                    |  (TextNode: 'this is ')
                    |  (TagNode:
                    |    (ThistleTagStartNode: 'one')
                    |    (ManyNode:
                    |      (TextNode: 'tag 1')
                    |    )
                    |  )
                    |)
                    """.trimMargin()
                )
        }

        "this is {{one}}a {{two}}tag 2{{/two}} inside tag 1{{/one}}".let { input ->
            val result = thistle.parser.parse(ParserContext.fromString(input))
            expectThat(result)
                .parsedCorrectly(
                    """
                    |(ManyNode:
                    |  (TextNode: 'this is ')
                    |  (TagNode:
                    |    (ThistleTagStartNode: 'one')
                    |    (ManyNode:
                    |      (TextNode: 'a ')
                    |      (TagNode:
                    |        (ThistleTagStartNode: 'two')
                    |        (ManyNode:
                    |          (TextNode: 'tag 2')
                    |        )
                    |      )
                    |      (TextNode: ' inside tag 1')
                    |    )
                    |  )
                    |)
                    """.trimMargin()
                )
        }
    }
}
