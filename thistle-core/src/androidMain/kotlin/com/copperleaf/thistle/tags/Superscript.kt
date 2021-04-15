package com.copperleaf.thistle.tags

import android.text.style.SuperscriptSpan
import com.copperleaf.thistle.parser.ThistleTag

class Superscript : ThistleTag {
    override fun invoke(args: Map<String, Any>): Any {
        return SuperscriptSpan()
    }
}
