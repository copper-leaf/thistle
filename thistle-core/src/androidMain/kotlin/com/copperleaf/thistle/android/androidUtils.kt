package com.copperleaf.thistle.android

import android.content.Context
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderer
import com.copperleaf.thistle.core.parser.ThistleParser

@ExperimentalStdlibApi
fun ThistleParser<AndroidThistleRenderContext, Any>.androidRenderer(uiContext: Context): AndroidThistleRenderer {
    return AndroidThistleRenderer(uiContext, tagFactories)
}

@ExperimentalStdlibApi
fun TextView.applyStyledText(
    thistle: ThistleParser<AndroidThistleRenderContext, Any>,
    input: String,
    context: Map<String, Any> = emptyMap()
) {
    val (rootNode, _) = thistle.parser.parse(ParserContext.fromString(input))
    val rendered = thistle.androidRenderer(this.context).render(rootNode, context)
    movementMethod = LinkMovementMethod.getInstance()
    text = rendered
}
