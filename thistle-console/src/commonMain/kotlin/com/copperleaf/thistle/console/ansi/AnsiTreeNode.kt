package com.copperleaf.thistle.console.ansi

@AnsiDsl
internal sealed class AnsiTreeNode {
    internal data class TextNode(
        var textContent: String
    ) : AnsiTreeNode()

    internal data class NestedNode(
        var ansiSequence: AnsiEscapeCode,
        val childrenContent: MutableList<AnsiTreeNode>
    ) : AnsiTreeNode()

    internal data class RootNode(
        val childrenContent: MutableList<AnsiTreeNode>
    ) : AnsiTreeNode()
}
