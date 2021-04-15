package com.copperleaf.thistle.renderer

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString

internal class AndroidThistleTagStringBuilder : ThistleTagStringBuilder {
    private val delegate = SpannableStringBuilder()

    override fun append(text: String) {
        delegate.append(text)
    }

    override fun pushTag(tag: Any, tagContentBuilder: ThistleTagStringBuilder.() -> Unit) {
        val start = delegate.length
        tagContentBuilder()
        delegate.setSpan(tag, start, delegate.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    fun toSpanned(): Spanned = SpannedString(delegate)
}
