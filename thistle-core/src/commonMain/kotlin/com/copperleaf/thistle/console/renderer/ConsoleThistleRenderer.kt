package com.copperleaf.thistle.console.renderer

import com.copperleaf.kudzu.node.Node
import com.copperleaf.thistle.core.parser.ThistleTagBuilder
import com.copperleaf.thistle.core.renderer.ThistleRenderer

@ExperimentalStdlibApi
class ConsoleThistleRenderer(
    tags: List<ThistleTagBuilder<AnsiEscapeCode>>
) : ThistleRenderer<AnsiEscapeCode, String>(tags) {

    override fun render(rootNode: Node, context: Map<String, Any>): String {
        return ConsoleThistleTagStringBuilder()
            .apply { renderToBuilder(rootNode, context) }
            .toString()
    }
}
