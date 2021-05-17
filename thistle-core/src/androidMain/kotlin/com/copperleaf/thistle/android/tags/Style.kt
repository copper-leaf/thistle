package com.copperleaf.thistle.android.tags

import android.graphics.Typeface
import android.text.style.StyleSpan
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class Style(
    private val hardcodedStyle: Int? = null
) : ThistleTag<Any> {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return checkArgs(args) {
            val style: Int by enum(hardcodedStyle) {
                mapOf(
                    "bold" to Typeface.BOLD,
                    "italic" to Typeface.ITALIC
                )
            }

            StyleSpan(style)
        }
    }
}
