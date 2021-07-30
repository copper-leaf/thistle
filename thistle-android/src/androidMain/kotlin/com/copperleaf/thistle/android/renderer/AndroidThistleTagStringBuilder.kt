package com.copperleaf.thistle.android.renderer

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString

internal class AndroidThistleTagStringBuilder(
    val uiContext: Context,
) {
    private val delegate = SpannableStringBuilder()

    fun append(text: String) {
        delegate.append(text)
    }

    fun pushTag(
        context: Map<String, Any>,
        args: Map<String, Any>,
        tagContentBuilder: AndroidThistleTagStringBuilder.() -> Unit
    ): AndroidThistleRenderContext {
        val start = delegate.length
        tagContentBuilder()
        val end = delegate.length

        val contentChars = CharArray(end - start)
        delegate.getChars(start, end, contentChars, 0)

        return AndroidThistleRenderContext(
            uiContext = uiContext,
            startIndex = start,
            endIndex = end,
            context = context,
            args = args,
            content = String(contentChars)
        )
    }

    fun setTag(
        renderContext: AndroidThistleRenderContext,
        tag: Any
    ) {
        delegate.setSpan(
            tag,
            renderContext.startIndex,
            renderContext.endIndex,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }

    fun toSpanned(): Spanned = SpannedString(delegate)
}
