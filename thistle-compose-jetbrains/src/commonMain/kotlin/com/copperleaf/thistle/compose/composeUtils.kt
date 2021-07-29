package com.copperleaf.thistle.compose

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.ParserException
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.parser.ThistleParser

/**
 * Parse the [input] string and render it to [AnnotatedString] for use as the content of a [Text] composable.
 *
 * The input string is parsed and the resulting AST is cached with [remember]. If an error happens during parsing, by
 * default Compose will crash with an exception, but it is handled internally and not exposed to the caller. To handle
 * this, you can provide a trailing lambda as [onErrorDefaultTo] to inspect the actual exception thrown by the parser,
 * and optionally return default text to display instead. A non-null value will be displayed. A null value will re-throw
 * the exception and Compose will crash.
 *
 * The renderer result is also cached with [remember], and will get re-evaluated every time [input] or [context]
 * changes.
 */
@ExperimentalStdlibApi
@Composable
fun rememberRichText(
    thistle: ThistleParser<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeRichText>,
    input: String,
    context: Map<String, Any> = emptyMap(),
    onErrorDefaultTo: ((ParserException)-> String)? = null
): ComposeRichText {
    val rootNode = remember(input) {
        val inputContext = ParserContext.fromString(input)
        try {
            thistle.parse(inputContext)
        }
        catch (e: ParserException) {
            onErrorDefaultTo
                ?.invoke(e)
                ?.let {
                    val nodeContext = NodeContext(inputContext, inputContext)
                    ThistleRootNode(
                        listOf(TextNode(it, nodeContext)),
                        nodeContext
                    )
                }
                ?: throw e
        }
    }
    val rendered = remember(context, rootNode) {
        thistle.newRenderer().render(rootNode, context)
    }
    return rendered
}

data class ComposeSpanWrapper(
    val spanStyle: SpanStyle? = null,
    val clickHandler: (()->Unit)? = null,
    val inlineContent: InlineTextContent? = null,
)

data class ComposeRichText(
    val result: AnnotatedString,
    private val clickHandlers: List<()->Unit>,
    val inlineContent: Map<String, InlineTextContent>,
) {
    fun onClick(offset: Int) {
        result
            .getStringAnnotations(
                tag = "thistleLinkHandler",
                start = offset,
                end = offset,
            )
            .firstOrNull()
            ?.let { annotation ->
                val index = annotation.item.toInt()
                clickHandlers[index].invoke()
            }
    }
}

fun Modifier.detectRichTextClicks(
    layoutResult: TextLayoutResult?,
    richText: ComposeRichText
): Modifier {
    return pointerInput(layoutResult, richText) {
        detectTapGestures { pos ->
            layoutResult?.let {
                richText.onClick(it.getOffsetForPosition(pos))
            }
        }
    }
}
