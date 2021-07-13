package com.copperleaf.thistle.app.ui.main

import android.content.Context
import android.text.Spanned
import android.util.Log
import com.copperleaf.kudzu.node.Node
import com.copperleaf.thistle.android.androidRenderer
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleParser
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalStdlibApi
@ExperimentalTime
@ExperimentalUnsignedTypes
data class InputCache(
    val text: String,
    val rootNode: Node,
    val astString: String,
) {
    fun render(
        uiContext: Context,
        thistle: ThistleParser<AndroidThistleRenderContext, Any>,
        context: Map<String, Any>
    ): Spanned {
        Log.i("InputCache", "Rendering thistle string... [$text]")

        val spanned: Spanned
        val duration = measureTime {
            spanned = thistle.androidRenderer(uiContext).render(rootNode, context)
        }

        Log.i("InputCache", "Rendering thistle string -> $duration")

        return spanned
    }
}
