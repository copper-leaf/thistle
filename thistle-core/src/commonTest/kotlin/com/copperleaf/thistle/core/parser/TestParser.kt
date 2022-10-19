package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.tag.TagNameNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode
import com.copperleaf.thistle.core.renderer.ThistleRenderContext
import com.copperleaf.thistle.core.renderer.ThistleRenderer
import com.copperleaf.thistle.expectCatching
import com.copperleaf.thistle.isA
import com.copperleaf.thistle.isNotNull
import com.copperleaf.thistle.node
import com.copperleaf.thistle.parsedCorrectly
import com.copperleaf.thistle.parsedIncorrectly
import com.copperleaf.thistle.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestParser : StringSpec({

    "testHexColorAsIntValueParser" {
        val underTest = ThistleSyntaxBuilder.hexColorAsIntValueParser
        "#00ff00".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext).parsedCorrectly()
        }

        "#00FF00".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext).parsedCorrectly()
        }

        "00ff00".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe false
            underTest.test(parserContext).parsedIncorrectly()
        }
    }

    "testUnquotedStringValueParser" {
        val underTest = ThistleSyntaxBuilder.unquotedStringValueValueParser
        val contextMap = emptyMap<String, Any>()

        "one".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe "one"
                }
        }
    }

    "testContextValueParser" {
        val underTest = ThistleSyntaxBuilder.contextValueParser
        val contextMap = mapOf("one" to 1, "two" to 2.2)

        "context.one".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 1
                }
        }
        "context.two".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 2.2
                }
        }
        "context.three".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    expectCatching { getValue(contextMap) }
                        .second
                        .isNotNull()
                        .isA<ThistleMissingContextValueException>()
                        .message shouldBe "At NodeContext(1:9 to 1:14): Context must contain value for key 'three'"
                }
        }
    }

    "testAttrValueParser" {
        val underTest = ThistleSyntaxBuilder<TestRenderContext, Any, Any>().buildAttrValueParser()
        val contextMap = mapOf("one" to 1, "two" to 2.2)

        // boolean value
        "true".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe true
                }
        }
        "false".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe false
                }
        }

        // double value
        "1.1".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 1.1
                }
        }

        // int value
        "2".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 2
                }
        }

        // string value
        "\"a string value\"".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe "a string value"
                }
        }

        // char value
        "'c'".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 'c'
                }
        }

        // unquoted string value
        "monospace".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe "monospace"
                }
        }

        // color value
        "#00ff00".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 0xFF00FF00u.toInt()
                }
        }
        "#888888".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 0xFF888888u.toInt()
                }
        }

        // context value
        "context.one".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 1
                }
        }
        "context.two".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    getValue(contextMap) shouldBe 2.2
                }
        }
        "context.three".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    expectCatching { getValue(contextMap) }
                        .second
                        .isNotNull()
                        .isA<ThistleMissingContextValueException>()
                        .message shouldBe "At NodeContext(1:9 to 1:14): Context must contain value for key 'three'"
                }
        }
    }

    "testAttrMapParser" {
        val underTest = ThistleSyntaxBuilder<TestRenderContext, Any, Any>().buildAttrMapParser()
        val contextMap = mapOf("one" to 1, "two" to 2.2)
        val input = "truthy=true " +
            "one=1 " +
            "two=2.2 " +
            "color=#00ff00 " +
            "string=\"a string value\" " +
            "char='c' " +
            "unquoted=monospace cxt1=context.one cxt2=context.two"

        // boolean value

        val parserContext = ParserContext.fromString(input)
        underTest.predict(parserContext) shouldBe true
        underTest.test(parserContext)
            .parsedCorrectly()
            .node()
            .isNotNull()
            .apply {
                getValueMap(contextMap) shouldBe mapOf(
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
            }
    }

    "testSyntaxTagStartParser" {
        val syntax = ThistleSyntaxBuilder<TestRenderContext, Any, Any>().buildSyntaxParser()
        val contextMap = mapOf("one" to 1, "two" to 2.2)
        val underTest = syntax.tagStart()

        "{{one".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
        }

        "{{two".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
        }

        "{{one}}".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    tagName shouldBe "one"
                    wrapped.getValueMap(contextMap) shouldBe emptyMap<String, Any>()
                }
        }

        "{{one one=1}}".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    tagName shouldBe "one"
                    wrapped.getValueMap(contextMap) shouldBe mapOf<String, Any>("one" to 1)
                }
        }
        "{{one one=context.four}}".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    tagName shouldBe "one"
                    expectCatching { wrapped.getValueMap(contextMap) }
                        .second
                        .isNotNull()
                        .isA<ThistleMissingContextValueException>()
                        .message shouldBe "At NodeContext(1:19 to 1:23): Context must contain value for key 'four'"
                }
        }
    }

    "testFullParser" {
        val thistle = ThistleParser(TestDefaults) {
            tag("one") { ThistleTagFactory { } }
        }
        val contextMap = mapOf("one" to 1, "two" to 2.2, "username" to "AliceBob123")
        val underTest = thistle

        "{{one}}two{{/one}}".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .nodeList
                .first()
                .isA<TagNode<*, *, *>>()
                .opening
                .isA<TagNameNode<ThistleValueMapNode>>()
                .apply {
                    tagName shouldBe "one"
                    wrapped.getValueMap(contextMap) shouldBe emptyMap<String, Any>()
                }
        }

        "{{one one=1}}two{{/one}}".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .nodeList
                .first()
                .isA<TagNode<*, *, *>>()
                .opening
                .isA<TagNameNode<ThistleValueMapNode>>()
                .apply {
                    tagName shouldBe "one"
                    wrapped.getValueMap(contextMap) shouldBe mapOf<String, Any>("one" to 1)
                }
        }
        "{{one one=context.four}}two{{/one}}".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .nodeList
                .first()
                .isA<TagNode<*, *, *>>()
                .opening
                .isA<TagNameNode<ThistleValueMapNode>>()
                .apply {
                    tagName shouldBe "one"
                    expectCatching { wrapped.getValueMap(contextMap) }
                        .second
                        .isNotNull()
                        .isA<ThistleMissingContextValueException>()
                        .message shouldBe "At NodeContext(1:19 to 1:23): Context must contain value for key 'four'"
                }
        }

        "interpolate {username} here".apply {
            val parserContext = ParserContext.fromString(this)
            underTest.predict(parserContext) shouldBe true
            underTest.test(parserContext, logErrors = true)
                .parsedCorrectly()
                .node()
                .isNotNull()
                .also {
                    val tagNode = it.nodeList[1].isA<TagNode<*, *, *>>()

                    val interpolateNode = tagNode.opening.wrapped as ThistleInterpolateNode
                    interpolateNode.key shouldBe "username"
                    interpolateNode.getValue(contextMap) shouldBe "AliceBob123"
                }
        }
    }
}) {

    companion object {
        class TestRenderContext(
            override val context: Map<String, Any> = emptyMap(),
            override val args: Map<String, Any> = emptyMap()
        ) : ThistleRenderContext

        class TestRenderer : ThistleRenderer<TestRenderContext, Any, Any>(emptyMap()) {
            override fun render(rootNode: ThistleRootNode, context: Map<String, Any>): Any {
                return Unit
            }
        }

        object TestDefaults : ThistleSyntaxBuilder.Defaults<TestRenderContext, Any, Any> {
            override fun applyToBuilder(builder: ThistleSyntaxBuilder<TestRenderContext, Any, Any>) {
                with(builder) {
                }
            }

            override fun rendererFactory(): ThistleRendererFactory<TestRenderContext, Any, Any> {
                return ThistleRendererFactory { TestRenderer() }
            }
        }
    }
}
