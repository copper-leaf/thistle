package com.copperleaf.thistle.android

import android.content.Context
import android.graphics.Typeface
import android.text.Spanned
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.thistle.android.parser.ResourceReferenceParser
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderer
import com.copperleaf.thistle.android.tags.AndroidBackgroundColor
import com.copperleaf.thistle.android.tags.AndroidForegroundColor
import com.copperleaf.thistle.android.tags.AndroidIcon
import com.copperleaf.thistle.android.tags.AndroidStrikethrough
import com.copperleaf.thistle.android.tags.AndroidStyle
import com.copperleaf.thistle.android.tags.AndroidSubscript
import com.copperleaf.thistle.android.tags.AndroidSuperscript
import com.copperleaf.thistle.android.tags.AndroidTypeface
import com.copperleaf.thistle.android.tags.AndroidUnderline
import com.copperleaf.thistle.android.tags.AndroidUrl
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

public class AndroidDefaults(
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
            tag("monospace") { AndroidTypeface(Typeface.MONOSPACE) }
            tag("sans") { AndroidTypeface(Typeface.SANS_SERIF) }
            tag("serif") { AndroidTypeface(Typeface.SERIF) }

            tag("strikethrough") { AndroidStrikethrough() }

            tag("subscript") { AndroidSubscript() }
            tag("superscript") { AndroidSuperscript() }

            tag("url") { AndroidUrl() }
            tag("icon") { AndroidIcon() }

            tag("b") { AndroidStyle(Typeface.BOLD) }
            tag("i") { AndroidStyle(Typeface.ITALIC) }
            tag("u") { AndroidUnderline() }

            valueFormat {
                MappedParser(ResourceReferenceParser()) {
                    it.getValue(uiContext, packageName)
                }.asThistleValueParser()
            }
        }
    }
}
