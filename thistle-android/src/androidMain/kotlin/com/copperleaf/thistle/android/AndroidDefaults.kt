package com.copperleaf.thistle.android

import android.content.Context
import android.text.Spanned
import com.copperleaf.thistle.android.parser.ResourceReferenceParser
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderer
import com.copperleaf.thistle.android.tags.BackgroundColor
import com.copperleaf.thistle.android.tags.ForegroundColor
import com.copperleaf.thistle.android.tags.Icon
import com.copperleaf.thistle.android.tags.Strikethrough
import com.copperleaf.thistle.android.tags.Style
import com.copperleaf.thistle.android.tags.Subscript
import com.copperleaf.thistle.android.tags.Superscript
import com.copperleaf.thistle.android.tags.Typeface
import com.copperleaf.thistle.android.tags.Underline
import com.copperleaf.thistle.android.tags.Url
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

@ExperimentalStdlibApi
class AndroidDefaults(
    private val uiContext: Context,
    private val packageName: String? = null
) : ThistleSyntaxBuilder.Defaults<AndroidThistleRenderContext, Any, Spanned> {

    override fun rendererFactory(): ThistleRendererFactory<AndroidThistleRenderContext, Any, Spanned> {
        return ThistleRendererFactory { AndroidThistleRenderer(uiContext, it) }
    }

    /**
     * Adds the default set of Android tags to the [ThistleSyntaxBuilder]. Also add a value format for `@`-type
     * resources defined in Android XML Resources, matching the format typically used in layouts. It currently only
     * recognizes `@color/`, `@string/`, and `@drawable/` resources
     */

    /**
     * Adds the default set of Android tags to the [ThistleSyntaxBuilder].
     */
    override fun applyToBuilder(builder: ThistleSyntaxBuilder<AndroidThistleRenderContext, Any, Spanned>) {
        with(builder) {
            tag("foreground") { ForegroundColor() }
            tag("background") { BackgroundColor() }

            tag("style") { Style() }

            tag("typeface") { Typeface() }
            tag("monospace") { Typeface(android.graphics.Typeface.MONOSPACE) }
            tag("sans") { Typeface(android.graphics.Typeface.MONOSPACE) }
            tag("serif") { Typeface(android.graphics.Typeface.MONOSPACE) }

            tag("strikethrough") { Strikethrough() }

            tag("subscript") { Subscript() }
            tag("superscript") { Superscript() }

            tag("url") { Url() }
            tag("icon") { Icon() }

            tag("b") { Style(android.graphics.Typeface.BOLD) }
            tag("i") { Style(android.graphics.Typeface.ITALIC) }
            tag("u") { Underline() }

            if (packageName != null) {
                valueFormat {
                    ResourceReferenceParser(uiContext, packageName).asThistleValueParser()
                }
            }
        }
    }
}
