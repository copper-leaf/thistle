package com.copperleaf.thistle.android.tags

import android.text.style.SubscriptSpan
import com.copperleaf.thistle.core.parser.ThistleTag

class Subscript : ThistleTag<Any> {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return SubscriptSpan()
    }
}
