package com.copperleaf.thistle.android.tags

import android.text.style.SuperscriptSpan
import com.copperleaf.thistle.core.parser.ThistleTag

class Superscript : ThistleTag<Any> {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return SuperscriptSpan()
    }
}
