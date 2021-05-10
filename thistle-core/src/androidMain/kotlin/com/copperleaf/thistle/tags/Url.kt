package com.copperleaf.thistle.tags

import android.text.style.URLSpan
import com.copperleaf.thistle.checkArgs
import com.copperleaf.thistle.parser.ThistleTag

class Url(
    private val hardcodedUrl: String? = null
) : ThistleTag {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return checkArgs(args) {
            val url: String by string(hardcodedUrl)

            URLSpan(url)
        }
    }
}
