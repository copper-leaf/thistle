package com.copperleaf.thistle.app.ui.main

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class ForegroundColorFromString : ThistleTag {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return checkArgs(args) {
            val color: String by string()

            ForegroundColorSpan(Color.parseColor(color))
        }
    }
}
