package com.copperleaf.thistle.app.ui.main

import android.text.Spanned
import android.util.Log
import com.copperleaf.kudzu.node.Node
import com.copperleaf.thistle.parser.ThistleParser
import com.copperleaf.thistle.androidRenderer
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
    fun render(thistle: ThistleParser, context: Map<String, Any>): Spanned {
        Log.i("InputCache", "Rendering thistle string... [$text]")

        val spanned: Spanned
        val duration = measureTime {
            spanned = thistle.androidRenderer.render(rootNode, context)
        }

        Log.i("InputCache", "Rendering thistle string -> $duration")

        return spanned
    }
}
