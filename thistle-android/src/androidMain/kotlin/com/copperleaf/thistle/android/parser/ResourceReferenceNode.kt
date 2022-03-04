package com.copperleaf.thistle.android.parser

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode

public sealed class ResourceReferenceNode<T : Any>(
    public val resourceType: String,
    public val resourceName: String,
    nodeContext: NodeContext
) : TerminalNode(nodeContext) {
    abstract fun getValue(
        uiContext: Context,
        packageName: String,
    ): T

    protected fun resourceId(
        uiContext: Context,
        packageName: String,
    ): Int = uiContext.resources.getIdentifier(
        resourceName,
        resourceType,
        packageName,
    )

    override val text: String get() = resourceName
}

public class StringResourceReferenceNode(
    resourceName: String,
    nodeContext: NodeContext,
) : ResourceReferenceNode<String>("string", resourceName, nodeContext) {
    override fun getValue(
        uiContext: Context,
        packageName: String,
    ): String {
        val id = resourceId(uiContext, packageName)
        return uiContext.resources.getString(id)
    }
}

public class ColorResourceReferenceNode(
    resourceName: String,
    nodeContext: NodeContext,
) : ResourceReferenceNode<Int>("color", resourceName, nodeContext) {

    @Suppress("DEPRECATION")
    override fun getValue(
        uiContext: Context,
        packageName: String,
    ): Int {
        val id = resourceId(uiContext, packageName)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiContext.resources.getColor(id, uiContext.theme)
        } else {
            uiContext.resources.getColor(id)
        }
    }
}

public class DrawableResourceReferenceNode(
    resourceName: String,
    nodeContext: NodeContext,
) : ResourceReferenceNode<Drawable>("drawable", resourceName, nodeContext) {

    @Suppress("DEPRECATION")
    override fun getValue(
        uiContext: Context,
        packageName: String,
    ): Drawable {
        val id = resourceId(uiContext, packageName)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiContext.resources.getDrawable(id, uiContext.theme)
        } else {
            uiContext.resources.getDrawable(id)
        }
    }
}
