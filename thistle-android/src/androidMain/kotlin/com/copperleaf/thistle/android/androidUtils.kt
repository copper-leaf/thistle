package com.copperleaf.thistle.android

import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleParser

public fun TextView.applyStyledText(
    thistle: ThistleParser<AndroidThistleRenderContext, Any, Spanned>,
    input: String,
    context: Map<String, Any> = emptyMap()
) {
    val rootNode = thistle.parse(ParserContext.fromString(input))
    val rendered = thistle.newRenderer().render(rootNode, context)
    movementMethod = LinkMovementMethod.getInstance()
    text = rendered
}
