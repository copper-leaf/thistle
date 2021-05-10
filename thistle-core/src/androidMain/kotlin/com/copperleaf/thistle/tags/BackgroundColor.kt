package com.copperleaf.thistle.tags

import android.text.style.BackgroundColorSpan
import com.copperleaf.thistle.checkArgs
import com.copperleaf.thistle.parser.ThistleTag

class BackgroundColor(
    private val hardcodedColor: Int? = null
) : ThistleTag {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return checkArgs(args) {
            val color: Int by int(hardcodedColor)

            BackgroundColorSpan(color)
        }
    }
}
