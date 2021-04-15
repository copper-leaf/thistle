package com.copperleaf.thistle.tags

import android.text.style.ForegroundColorSpan
import com.copperleaf.thistle.checkArgs
import com.copperleaf.thistle.parser.ThistleTag

class ForegroundColor(
    private val hardcodedColor: Int? = null
) : ThistleTag {
    override fun invoke(args: Map<String, Any>): Any {
        return checkArgs(args) {
            val color: Int by int(hardcodedColor)

            ForegroundColorSpan(color)
        }
    }
}
