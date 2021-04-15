package com.copperleaf.thistle.renderer

import android.text.Spanned
import com.copperleaf.kudzu.node.Node
import com.copperleaf.thistle.parser.ThistleTagBuilder

@ExperimentalStdlibApi
class AndroidThistleRenderer(
    tags: List<ThistleTagBuilder>
) : ThistleRenderer<Spanned>(tags) {

    override fun render(rootNode: Node): Spanned {
        return AndroidThistleTagStringBuilder()
            .apply { renderToBuilder(rootNode) }
            .toSpanned()
    }
}
