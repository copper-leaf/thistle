package com.copperleaf.thistle.android.tags

import android.text.style.ForegroundColorSpan
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class ForegroundColor(
    private val hardcodedColor: Int? = null
) : ThistleTag<Any> {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return checkArgs(args) {
            val color: Int by int(hardcodedColor)

            ForegroundColorSpan(color)
        }
    }
}
