package com.copperleaf.thistle

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.ParserException
import com.copperleaf.kudzu.parser.ParserResult

fun <T : Node> ParserResult<T>?.parsedCorrectly(
    expected: String? = null,
    allowRemaining: Boolean = false
): ParserResult<T> {
    if (this == null) error(
        "Subject cannot be null"
    )
    else {
        if (expected != null) {
            when (first.toString()) {
                expected.trimIndent().trim() -> {
                }

                else -> error(
                    "Output AST should be $expected, got $first"
                )
            }
        }
        if (!allowRemaining) {
            when (second.isEmpty()) {
                true -> {
                }

                else -> error(
                    "There should be nothing remaining, still had $second",
                )
            }
        }
    }

    return this
}

fun ParserResult<Node>?.parsedIncorrectly(): ParserResult<Node>? {
    if (this == null) {
    } else {
        when (!second.isEmpty()) {
            true -> {
            }

            else -> error("Subject must be null or have input remaining. Actual: $second")
        }
    }

    return this
}

fun <T : Node> ParserResult<T>?.node(): T? = this?.first

fun <T : Any> expectCatching(value: () -> T): Pair<T?, Throwable?> {
    try {
        val evaluated = value()
        return evaluated to null
    } catch (t: Throwable) {
        return null to t
    }
}

fun <T, U> T.get(block: T.() -> U): U {
    return block()
}

inline fun <reified T> Any.isA(): T {
    check(this is T) { "Expected this to be ${T::class}, got $this" }

    return this
}

fun <T> T?.isNotNull(): T {
    return checkNotNull(this) { "Expected value to be non-null, but it was null" }
}

fun <NodeType : Node> Parser<NodeType>.test(
    input: ParserContext,
    logErrors: Boolean = false
): Pair<NodeType, ParserContext>? {
    return try {
        parse(input)
    } catch (e: ParserException) {
        if (logErrors) {
            e.printStackTrace()
            println("Parsing failed for input: '$input'")
        }
        null
    }
}
