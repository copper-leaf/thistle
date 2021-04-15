package com.copperleaf.thistle.tags

import android.text.style.UnderlineSpan
import com.copperleaf.thistle.parser.ThistleTag

class Underline : ThistleTag {
    override fun invoke(args: Map<String, Any>): Any {
        return UnderlineSpan()
    }
}
