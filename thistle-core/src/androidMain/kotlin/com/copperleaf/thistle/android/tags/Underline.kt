package com.copperleaf.thistle.android.tags

import android.text.style.UnderlineSpan
import com.copperleaf.thistle.core.parser.ThistleTag

class Underline : ThistleTag<Any> {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return UnderlineSpan()
    }
}
