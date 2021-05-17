package com.copperleaf.thistle.android.renderer

import android.text.Spanned
import com.copperleaf.kudzu.node.Node
import com.copperleaf.thistle.core.parser.ThistleTagBuilder
import com.copperleaf.thistle.core.renderer.ThistleRenderer

@ExperimentalStdlibApi
class AndroidThistleRenderer(
    tags: List<ThistleTagBuilder<Any>>
) : ThistleRenderer<Any, Spanned>(tags) {

    override fun render(rootNode: Node, context: Map<String, Any>): Spanned {
        return AndroidThistleTagStringBuilder()
            .apply { renderToBuilder(rootNode, context) }
            .toSpanned()
    }
}
