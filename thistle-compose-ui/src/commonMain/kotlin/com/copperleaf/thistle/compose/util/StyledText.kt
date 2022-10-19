package com.copperleaf.thistle.compose.util

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.ParserException
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.parser.ThistleParser
import com.copperleaf.thistle.core.parser.ThistleUnknownTagException

/**
 * Parse the [input] string and render it to [AnnotatedString] for use as the content of a [Text] composable. By
 * default, this uses the current values of [LocalThistle] and [LocalThistleContext] to allow you to configure Thistle
 * once at the screen root, but you can override these if needed.
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
@Composable
public fun rememberStyledText(
    input: String,
    thistle: ThistleParser<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeStyledText> = LocalThistle.current,
    context: Map<String, Any> = LocalThistleContext.current,
    onErrorDefaultTo: ((Throwable) -> String)? = null
): ComposeStyledText {
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

public data class ComposeSpanWrapper(
    val spanStyle: SpanStyle? = null,
    val clickHandler: (() -> Unit)? = null,
    val inlineContent: InlineTextContent? = null,
)

public sealed class ComposeStyledText {

    public abstract val ast: ThistleRootNode

    public data class Success(
        override val ast: ThistleRootNode,
        internal val result: AnnotatedString,
        private val clickHandlers: List<() -> Unit>,
        internal val inlineContent: Map<String, InlineTextContent>,
    ) : ComposeStyledText() {
        internal fun onClick(offset: Int, noLinkClickedHandler: () -> Unit) {
            val clickedAnnotation = result
                .getStringAnnotations(
                    tag = "thistleLinkHandler",
                    start = offset,
                    end = offset,
                )
                .firstOrNull()

            if (clickedAnnotation != null) {
                // the click corresponded to a clickable region of text. Invoke its registered callback function.
                val index = clickedAnnotation.item.toInt()
                clickHandlers[index].invoke()
            } else {
                // the click was intercepted by the BasicText, but did not correspond to a clickable region of text.
                // It may have been another annotation, or have been unstyled text. Either way, we can't tell Compose to
                // not consume the click such that it bubbles up to the parent, so we have to manually notify that the
                // click was not handled by the StyledText and should fall back to the default click handler.
                noLinkClickedHandler()
            }
        }
    }

    public data class Failure(
        override val ast: ThistleRootNode,
        public val throwable: Throwable,
    ) : ComposeStyledText()
}

public fun Modifier.detectStyledTextClicks(
    layoutResult: TextLayoutResult?,
    styledText: ComposeStyledText,
    noLinkClickedHandler: () -> Unit,
): Modifier {
    return this.pointerInput(layoutResult, styledText) {
        detectTapGestures { pos ->
            if (layoutResult != null) {
                when (styledText) {
                    is ComposeStyledText.Failure -> {
                        // the text was not parsed correctly, so treat all clicks as if they were intercepted by not
                        // handled, to fall back to the default click handler
                        noLinkClickedHandler()
                    }
                    is ComposeStyledText.Success -> {
                        // the text was parsed correctly. Attempt to figure out if the click corresponded to a clickable
                        // region of text, or else fall back to the default click handler
                        styledText.onClick(
                            offset = layoutResult.getOffsetForPosition(pos),
                            noLinkClickedHandler = noLinkClickedHandler,
                        )
                    }
                }
            } else {
                // ignore, the text is not ready yet
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
@Composable
public fun StyledText(
    text: String,
    modifier: Modifier = Modifier,
    thistle: ThistleParser<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeStyledText> = LocalThistle.current,
    context: Map<String, Any> = LocalThistleContext.current,
    onErrorDefaultTo: ((Throwable) -> String)? = null,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    noLinkClickedHandler: () -> Unit = { },
    onTextLayout: (TextLayoutResult) -> Unit = { },
) {
    val styledText = rememberStyledText(
        input = text,
        thistle = thistle,
        context = context,
        onErrorDefaultTo = onErrorDefaultTo,
    )
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val displayedText: AnnotatedString by derivedStateOf {
        when (styledText) {
            is ComposeStyledText.Success -> styledText.result
            is ComposeStyledText.Failure -> buildAnnotatedString {
                append(onErrorDefaultTo?.invoke(styledText.throwable as Exception) ?: "")
            }
        }
    }

    val inlineContent: Map<String, InlineTextContent> by derivedStateOf {
        when (styledText) {
            is ComposeStyledText.Success -> styledText.inlineContent
            is ComposeStyledText.Failure -> emptyMap()
        }
    }

    BasicText(
        text = displayedText,
        inlineContent = inlineContent,
        modifier = modifier
            .detectStyledTextClicks(layoutResult.value, styledText, noLinkClickedHandler),
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
