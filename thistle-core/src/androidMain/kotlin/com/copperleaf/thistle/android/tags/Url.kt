package com.copperleaf.thistle.android.tags

import android.text.style.URLSpan
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

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
