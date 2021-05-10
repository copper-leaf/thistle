package com.copperleaf.thistle.parser

import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.expectCatching
import com.copperleaf.thistle.expectThat
import com.copperleaf.thistle.isA
import com.copperleaf.thistle.isEqualTo
import com.copperleaf.thistle.isFalse
import com.copperleaf.thistle.isNotNull
import com.copperleaf.thistle.isTrue
import com.copperleaf.thistle.node
import com.copperleaf.thistle.node.ThistleTagStartNode
import com.copperleaf.thistle.parsedCorrectly
import com.copperleaf.thistle.parsedIncorrectly
import com.copperleaf.thistle.test
import kotlin.test.Test

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
class TestParser {

    @Test
    fun testHexColorAsIntValueParser() {
        val underTest = ThistleSyntaxBuilder.hexColorAsIntValueParser
        "#00ff00".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext).parsedCorrectly()
        }

        "#00FF00".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext).parsedCorrectly()
        }

        "00ff00".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isFalse()
            underTest.test(parserContext).parsedIncorrectly()
        }
    }

    @Test
    fun testUnquotedStringValueParser() {
        val underTest = ThistleSyntaxBuilder.unquotedStringValueValueParser
        val contextMap = emptyMap<String, Any>()

        "one".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo("one")
                }
        }
    }

    @Test
    fun testContextValueParser() {
        val underTest = ThistleSyntaxBuilder.contextValueParser
        val contextMap = mapOf("one" to 1, "two" to 2.2)

        "context.one".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(1)
                }
        }
        "context.two".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(2.2)
                }
        }
        "context.three".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    expectCatching { getValue(contextMap) }
                        .second
                        .isNotNull()
                        .isA<IllegalStateException>()
                        .message
                        .isEqualTo("Error: Context must contain value for key 'three'")
                }
        }
    }

    @Test
    fun testAttrValueParser() {
        val underTest = ThistleSyntaxBuilder().buildAttrValueParser()
        val contextMap = mapOf("one" to 1, "two" to 2.2)

        // boolean value
        "true".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(true)
                }
        }
        "false".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(false)
                }
        }

        // double value
        "1.1".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(1.1)
                }
        }

        // int value
        "2".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(2)
                }
        }

        // string value
        "\"a string value\"".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo("a string value")
                }
        }

        // char value
        "'c'".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo('c')
                }
        }

        // unquoted string value
        "monospace".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo("monospace")
                }
        }

        // color value
        "#00ff00".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(0xFF00FF00u.toInt())
                }
        }
        "#888888".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(0xFF888888u.toInt())
                }
        }

        // context value
        "context.one".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(1)
                }
        }
        "context.two".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap).isEqualTo(2.2)
                }
        }
        "context.three".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    expectCatching { getValue(contextMap) }
                        .second
                        .isNotNull()
                        .isA<IllegalStateException>()
                        .message
                        .isEqualTo("Error: Context must contain value for key 'three'")
                }
        }
    }

    @Test
    fun testAttrMapParser() {
        val underTest = ThistleSyntaxBuilder().buildAttrMapParser()
        val contextMap = mapOf("one" to 1, "two" to 2.2)

        // boolean value
        ("truthy=true " +
                "one=1 " +
                "two=2.2 " +
                "color=#00ff00 " +
                "string=\"a string value\" " +
                "char='c' " +
                "unquoted=monospace cxt1=context.one cxt2=context.two").apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValueMap(contextMap).isEqualTo(
                        mapOf(
                            "truthy" to true,
                            "one" to 1,
                            "two" to 2.2,
                            "color" to 0xFF00FF00u.toInt(),
                            "string" to "a string value",
                            "char" to 'c',
                            "unquoted" to "monospace",
                            "cxt1" to 1,
                            "cxt2" to 2.2,
                        )
                    )
                }
        }
    }

    @Test
    fun testSyntaxTagStartParser() {
        val syntax = ThistleSyntaxBuilder().buildSyntaxParser()
        val contextMap = mapOf("one" to 1, "two" to 2.2)
        val underTest = syntax.tagStart("one")

        "{{one".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
        }

        "{{two".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isFalse()
        }

        "{{one}}".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    tagName.isEqualTo("one")
                    tagArgs.getValueMap(contextMap).isEqualTo(emptyMap<String, Any>())
                }
        }

        "{{one one=1}}".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    tagName.isEqualTo("one")
                    tagArgs.getValueMap(contextMap).isEqualTo(mapOf<String, Any>("one" to 1))
                }
        }
        "{{one one=context.four}}".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    tagName.isEqualTo("one")
                    expectCatching { tagArgs.getValueMap(contextMap) }
                        .second
                        .isNotNull()
                        .isA<IllegalStateException>()
                        .message
                        .isEqualTo("Error: Context must contain value for key 'four'")
                }
        }
    }

    @Test
    fun testFullParser() {
        val thistle = ThistleParser {
            tag("one") { ThistleTag { _, _ -> } }
        }
        val contextMap = mapOf("one" to 1, "two" to 2.2)
        val underTest = thistle.parser

        "{{one}}two{{/one}}".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .nodeList
                .first()
                .isA<TagNode<*, *>>()
                .opening
                .isA<ThistleTagStartNode>()
                .apply {
                    tagName.isEqualTo("one")
                    tagArgs.getValueMap(contextMap).isEqualTo(emptyMap<String, Any>())
                }
        }

        "{{one one=1}}two{{/one}}".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .nodeList
                .first()
                .isA<TagNode<*, *>>()
                .opening
                .isA<ThistleTagStartNode>()
                .apply {
                    tagName.isEqualTo("one")
                    tagArgs.getValueMap(contextMap).isEqualTo(mapOf<String, Any>("one" to 1))
                }
        }
        "{{one one=context.four}}two{{/one}}".apply {
            val parserContext = ParserContext.fromString(this)
            expectThat(underTest.predict(parserContext)).isTrue()
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .nodeList
                .first()
                .isA<TagNode<*, *>>()
                .opening
                .isA<ThistleTagStartNode>()
                .apply {
                    tagName.isEqualTo("one")
                    expectCatching { tagArgs.getValueMap(contextMap) }
                        .second
                        .isNotNull()
                        .isA<IllegalStateException>()
                        .message
                        .isEqualTo("Error: Context must contain value for key 'four'")
                }
        }
    }
}
