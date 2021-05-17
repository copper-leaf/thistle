package com.copperleaf.thistle.android.tags

import android.text.style.StrikethroughSpan
import com.copperleaf.thistle.core.parser.ThistleTag

class Strikethrough : ThistleTag {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return StrikethroughSpan()
    }
}
