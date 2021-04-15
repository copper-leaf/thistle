package com.copperleaf.thistle

import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.parser.ThistleParser
import com.copperleaf.thistle.renderer.AndroidThistleRenderer

@ExperimentalStdlibApi
val ThistleParser.renderer: AndroidThistleRenderer
    get() {
        return AndroidThistleRenderer(tags)
    }

@ExperimentalStdlibApi
fun TextView.applyStyledText(thistle: ThistleParser, input: String) {
    val (rootNode, _) = thistle.parser.parse(ParserContext.fromString(input))
    val rendered = thistle.renderer.render(rootNode)
    movementMethod = LinkMovementMethod.getInstance()
    text = rendered
}
