package com.copperleaf.thistle.compose.util

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.ParserException
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.parser.ThistleParser
import com.copperleaf.thistle.core.parser.ThistleUnknownTagException
import com.copperleaf.thistle.core.renderer.ThistleTagsArgs
import kotlin.properties.ReadOnlyProperty

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
    input: String,
    thistle: ThistleParser<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeRichText> = LocalThistle.current,
    context: Map<String, Any> = LocalThistleContext.current,
    onErrorDefaultTo: ((Throwable) -> String)? = null
): ComposeRichText {
    val rootNode = remember(input) {
        val inputContext = ParserContext.fromString(input)

        val errorHandler = { e: Throwable ->
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

        try {
            thistle.parse(inputContext)
        } catch (e: ParserException) {
            errorHandler(e)
        } catch (e: ThistleUnknownTagException) {
            errorHandler(e)
        }
    }
    val rendered = remember(context, rootNode) {
        thistle.newRenderer().render(rootNode, context)
    }
    return rendered
}

data class ComposeSpanWrapper(
    val spanStyle: SpanStyle? = null,
    val clickHandler: (() -> Unit)? = null,
    val inlineContent: InlineTextContent? = null,
)

sealed class ComposeRichText {

    abstract val ast: ThistleRootNode

    data class Success(
        override val ast: ThistleRootNode,
        val result: AnnotatedString,
        private val clickHandlers: List<()->Unit>,
        val inlineContent: Map<String, InlineTextContent>,
    ) : ComposeRichText() {
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
    data class Failure(
        override val ast: ThistleRootNode,
        val throwable: Throwable,
    ) : ComposeRichText()
}

fun Modifier.detectRichTextClicks(
    layoutResult: TextLayoutResult?,
    richText: ComposeRichText
): Modifier {
    return this.pointerInput(layoutResult, richText) {
        detectTapGestures { pos ->
            layoutResult?.let {
                (richText as? ComposeRichText.Success)?.onClick(it.getOffsetForPosition(pos))
            }
        }
    }
}


/**
 * A continent version of [BasicText] component to be able to handle click event on the text.
 *
 * This is a shorthand of [BasicText] with [pointerInput] to be able to handle click
 * event easily.
 *
 * @sample androidx.compose.foundation.samples.ClickableText
 *
 * For other gestures, e.g. long press, dragging, follow sample code.
 *
 * @sample androidx.compose.foundation.samples.LongClickableText
 *
 * @see BasicText
 * @see androidx.compose.ui.input.pointer.pointerInput
 * @see androidx.compose.foundation.gestures.detectTapGestures
 *
 * @param text The text to be displayed.
 * @param modifier Modifier to apply to this layout node.
 * @param style Style configuration for the text such as color, font, line height etc.
 * @param softWrap Whether the text should break at soft line breaks. If false, the glyphs in the
 * text will be positioned as if there was unlimited horizontal space. If [softWrap] is false,
 * [overflow] and [TextAlign] may have unexpected effects.
 * @param overflow How visual overflow should be handled.
 * @param maxLines An optional maximum number of lines for the text to span, wrapping if
 * necessary. If the text exceeds the given number of lines, it will be truncated according to
 * [overflow] and [softWrap]. If it is not null, then it must be greater than zero.
 * @param onTextLayout Callback that is executed when a new text layout is calculated. A
 * [TextLayoutResult] object that callback provides contains paragraph information, size of the
 * text, baselines and other details. The callback can be used to add additional decoration or
 * functionality to the text. For example, to draw selection around the text.
 * @param onClick Callback that is executed when users click the text. This callback is called
 * with clicked character's offset.
 */
@ExperimentalStdlibApi
@Composable
fun RichText(
    text: String,
    modifier: Modifier = Modifier,
    thistle: ThistleParser<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeRichText> = LocalThistle.current,
    context: Map<String, Any> = LocalThistleContext.current,
    onErrorDefaultTo: ((Throwable) -> String)? = null,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    val richText = rememberRichText(
        input = text,
        thistle = thistle,
        context = context,
        onErrorDefaultTo = onErrorDefaultTo,
    )
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val displayedText: AnnotatedString by derivedStateOf {
        when(richText) {
            is ComposeRichText.Success -> richText.result
            is ComposeRichText.Failure -> buildAnnotatedString {
                append(onErrorDefaultTo?.invoke(richText.throwable as Exception) ?: "")
            }
        }
    }

    BasicText(
        text = displayedText,
        modifier = modifier
            .detectRichTextClicks(layoutResult.value, richText),
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        }
    )
}
