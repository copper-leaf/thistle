package com.copperleaf.thistle.android.renderer

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import com.copperleaf.thistle.core.renderer.ThistleTagStringBuilder

internal class AndroidThistleTagStringBuilder : ThistleTagStringBuilder<Any> {
    private val delegate = SpannableStringBuilder()

    override fun append(text: String) {
        delegate.append(text)
    }

    override fun pushTag(tag: Any, tagContentBuilder: ThistleTagStringBuilder<Any>.() -> Unit) {
        val start = delegate.length
        tagContentBuilder()
        delegate.setSpan(tag, start, delegate.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    fun toSpanned(): Spanned = SpannedString(delegate)
}
