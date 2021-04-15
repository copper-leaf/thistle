package com.copperleaf.thistle.tags

import android.text.style.SubscriptSpan
import com.copperleaf.thistle.parser.ThistleTag

class Subscript : ThistleTag {
    override fun invoke(args: Map<String, Any>): Any {
        return SubscriptSpan()
    }
}
