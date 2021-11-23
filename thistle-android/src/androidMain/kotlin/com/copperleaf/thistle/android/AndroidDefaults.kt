package com.copperleaf.thistle.android

import android.content.Context
import android.text.Spanned
import com.copperleaf.thistle.android.parser.ResourceReferenceParser
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderer
import com.copperleaf.thistle.android.tags.AndroidBackgroundColor
import com.copperleaf.thistle.android.tags.AndroidForegroundColor
import com.copperleaf.thistle.android.tags.Icon
import com.copperleaf.thistle.android.tags.Strikethrough
import com.copperleaf.thistle.android.tags.AndroidStyle
import com.copperleaf.thistle.android.tags.Subscript
import com.copperleaf.thistle.android.tags.Superscript
import com.copperleaf.thistle.android.tags.AndroidTypeface
import com.copperleaf.thistle.android.tags.Underline
import com.copperleaf.thistle.android.tags.AndroidUrl
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

@ExperimentalStdlibApi
class AndroidDefaults(
    private val uiContext: Context,
    private val packageName: String = uiContext.applicationContext.packageName
) : ThistleSyntaxBuilder.Defaults<AndroidThistleRenderContext, Any, Spanned> {

    override fun rendererFactory(): ThistleRendererFactory<AndroidThistleRenderContext, Any, Spanned> {
        return ThistleRendererFactory { AndroidThistleRenderer(uiContext, it) }
    }

    /**
     * Adds the default set of Android tags to the [ThistleSyntaxBuilder]. Also add a value format for `@`-type
     * resources defined in Android XML Resources, matching the format typically used in layouts. It currently only
     * recognizes `@color/`, `@string/`, and `@drawable/` resources.
     */
    override fun applyToBuilder(builder: ThistleSyntaxBuilder<AndroidThistleRenderContext, Any, Spanned>) {
        with(builder) {
            tag("foreground") { AndroidForegroundColor() }
            tag("background") { AndroidBackgroundColor() }

            tag("style") { AndroidStyle() }

            tag("typeface") { AndroidTypeface() }
            tag("monospace") { AndroidTypeface(android.graphics.Typeface.MONOSPACE) }
            tag("sans") { AndroidTypeface(android.graphics.Typeface.MONOSPACE) }
            tag("serif") { AndroidTypeface(android.graphics.Typeface.MONOSPACE) }

            tag("strikethrough") { Strikethrough() }

            tag("subscript") { Subscript() }
            tag("superscript") { Superscript() }

            tag("url") { AndroidUrl() }
            tag("icon") { Icon() }

            tag("b") { AndroidStyle(android.graphics.Typeface.BOLD) }
            tag("i") { AndroidStyle(android.graphics.Typeface.ITALIC) }
            tag("u") { Underline() }

            valueFormat {
                ResourceReferenceParser(uiContext, packageName).asThistleValueParser()
            }
        }
    }
}
