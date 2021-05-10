package com.copperleaf.thistle.tags

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.style.TypefaceSpan
import com.copperleaf.thistle.checkArgs
import com.copperleaf.thistle.parser.ThistleTag

class Typeface(
    private val hardcodedTypeface: Typeface? = null
) : ThistleTag {

    @SuppressLint("NewApi")
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return checkArgs(args) {
            val typeface: Typeface by enum(hardcodedTypeface) {
                mapOf(
                    "monospace" to Typeface.MONOSPACE,
                    "sans" to Typeface.SANS_SERIF,
                    "serif" to Typeface.SERIF,
                )
            }

            TypefaceSpan(typeface)
        }
    }
}
