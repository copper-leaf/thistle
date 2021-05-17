package com.copperleaf.thistle.android.tags

import android.text.style.ClickableSpan
import android.view.View
import com.copperleaf.thistle.core.parser.ThistleTag

class Link(val handler: (widget: View) -> Unit) : ThistleTag {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                handler(widget)
            }
        }
    }
}
