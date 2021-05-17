package com.copperleaf.thistle.android

import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderer
import com.copperleaf.thistle.core.parser.ThistleParser

@ExperimentalStdlibApi
val ThistleParser<Any>.androidRenderer: AndroidThistleRenderer
    get() {
        return AndroidThistleRenderer(tags)
    }

@ExperimentalStdlibApi
fun TextView.applyStyledText(
    thistle: ThistleParser<Any>,
    input: String,
    context: Map<String, Any> = emptyMap()
) {
    val (rootNode, _) = thistle.parser.parse(ParserContext.fromString(input))
    val rendered = thistle.androidRenderer.render(rootNode, context)
    movementMethod = LinkMovementMethod.getInstance()
    text = rendered
}
