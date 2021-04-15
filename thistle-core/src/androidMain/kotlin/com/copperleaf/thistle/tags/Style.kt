package com.copperleaf.thistle.tags

import android.graphics.Typeface
import android.text.style.StyleSpan
import com.copperleaf.thistle.checkArgs
import com.copperleaf.thistle.parser.ThistleTag

class Style(
    private val hardcodedStyle: Int? = null
) : ThistleTag {
    override fun invoke(args: Map<String, Any>): Any {
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
