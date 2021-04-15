package com.copperleaf.thistle.tags

import android.text.style.StrikethroughSpan
import com.copperleaf.thistle.parser.ThistleTag

class Strikethrough : ThistleTag {
    override fun invoke(args: Map<String, Any>): Any {
        return StrikethroughSpan()
    }
}
