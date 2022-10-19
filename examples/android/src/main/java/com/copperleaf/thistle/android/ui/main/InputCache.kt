package com.copperleaf.thistle.android.ui.main

import android.text.Spanned
import android.util.Log
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.parser.ThistleParser
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
data class InputCache(
    val text: String,
    val rootNode: ThistleRootNode,
    val astString: String,
) {
    fun render(
        thistle: ThistleParser<AndroidThistleRenderContext, Any, Spanned>,
        context: Map<String, Any>
    ): Spanned {
        Log.i("InputCache", "Rendering thistle string... [$text]")

        val spanned: Spanned
        val duration = measureTime {
            spanned = thistle.newRenderer().render(rootNode, context)
        }

        Log.i("InputCache", "Rendering thistle string -> $duration")

        return spanned
    }
}
