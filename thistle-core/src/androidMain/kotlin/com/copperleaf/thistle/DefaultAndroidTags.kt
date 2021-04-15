package com.copperleaf.thistle

import android.annotation.SuppressLint
import com.copperleaf.thistle.parser.ThistleSyntaxBuilder
import com.copperleaf.thistle.tags.BackgroundColor
import com.copperleaf.thistle.tags.ForegroundColor
import com.copperleaf.thistle.tags.Strikethrough
import com.copperleaf.thistle.tags.Style
import com.copperleaf.thistle.tags.Subscript
import com.copperleaf.thistle.tags.Superscript
import com.copperleaf.thistle.tags.Typeface
import com.copperleaf.thistle.tags.Underline
import com.copperleaf.thistle.tags.Url

@ExperimentalStdlibApi
@SuppressLint("NewApi")
object DefaultAndroidTags : (ThistleSyntaxBuilder) -> Unit {
    override operator fun invoke(syntax: ThistleSyntaxBuilder) = with(syntax) {
        tag("foreground") { ForegroundColor() }
        tag("background") { BackgroundColor() }

        tag("style") { Style() }
        tag("b") { Style(android.graphics.Typeface.BOLD) }
        tag("i") { Style(android.graphics.Typeface.ITALIC) }
        tag("u") { Underline() }

        tag("typeface") { Typeface() }
        tag("monospace") { Typeface(android.graphics.Typeface.MONOSPACE) }
        tag("sans") { Typeface(android.graphics.Typeface.MONOSPACE) }
        tag("serif") { Typeface(android.graphics.Typeface.MONOSPACE) }

        tag("strikethrough") { Strikethrough() }

        tag("subscript") { Subscript() }
        tag("superscript") { Superscript() }

        tag("url") { Url() }
    }
}
