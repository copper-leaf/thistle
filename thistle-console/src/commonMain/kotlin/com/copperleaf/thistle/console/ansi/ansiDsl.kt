package com.copperleaf.thistle.console.ansi

@DslMarker
internal annotation class AnsiDsl

internal fun AnsiTreeNode.flatten(
    parentCodes: List<AnsiEscapeCode> = emptyList()
): List<AnsiStringComponent> {
    return when (this) {
        is AnsiTreeNode.NestedNode -> {
            this.childrenContent.flatMap { it.flatten(parentCodes + this.ansiSequence) }
        }
        is AnsiTreeNode.RootNode -> {
            this.childrenContent.flatMap { it.flatten() }
        }
        is AnsiTreeNode.TextNode -> {
            listOf(AnsiStringComponent(parentCodes, this.textContent))
        }
    }
}

internal fun List<AnsiStringComponent>.renderToString(): String {
    return this.joinToString(separator = "") { it.renderComponent() }
}

internal interface AnsiNodeScope {

    public fun appendText(textContent: String)

    public fun appendChild(ansiEscapeCode: (String) -> AnsiEscapeCode, block: AnsiNodeScope.() -> Unit)
}

internal class AnsiNodeScopeImpl : AnsiNodeScope {
    private val nodes = mutableListOf<AnsiTreeNode>()

    private fun textContent(): String {
        return nodes.flatMap { it.flatten() }.filter { it.textContent.isNotBlank() }
            .joinToString(separator = "") { it.textContent }
    }

    override fun appendText(textContent: String) {
        AnsiTreeNode.TextNode(textContent)
            .also { nodes.add(it) }
    }

    override fun appendChild(ansiEscapeCode: (String) -> AnsiEscapeCode, block: AnsiNodeScope.() -> Unit) {
        AnsiNodeScopeImpl()
            .apply(block)
            .let { it.toNestedNode(ansiEscapeCode(it.textContent())) }
            .also { nodes.add(it) }
    }

    internal fun toNestedNode(ansiEscapeCode: AnsiEscapeCode): AnsiTreeNode {
        return AnsiTreeNode.NestedNode(ansiEscapeCode, nodes)
    }

    internal fun toRootNode(): AnsiTreeNode {
        return AnsiTreeNode.RootNode(nodes)
    }
}

internal fun buildAnsiString(block: AnsiNodeScope.() -> Unit): AnsiTreeNode {
    return AnsiNodeScopeImpl()
        .apply(block)
        .toRootNode()
}
