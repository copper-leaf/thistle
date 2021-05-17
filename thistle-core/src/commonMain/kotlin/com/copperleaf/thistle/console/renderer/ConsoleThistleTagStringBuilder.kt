package com.copperleaf.thistle.console.renderer

import com.copperleaf.thistle.core.renderer.ThistleTagStringBuilder

internal class ConsoleThistleTagStringBuilder : ThistleTagStringBuilder<AnsiEscapeCode> {
    companion object {
        internal const val ansiControlCode = '\u001B'
        internal const val ansiReset = "[0m"
    }

    private val currentSequenceStack = mutableListOf<AnsiEscapeCode>()
    private val delegate = StringBuilder()

    override fun append(text: String) {
        delegate.append(text)
    }

    override fun pushTag(tag: AnsiEscapeCode, tagContentBuilder: ThistleTagStringBuilder<AnsiEscapeCode>.() -> Unit) {
        // push this tag into the escape code stack
        currentSequenceStack.add(tag)

        // start the ANSI control code for the latest tag
        delegate.append(ansiControlCode)
        delegate.append(tag.code)

        tagContentBuilder(this)

        // reset the ANSI control codes
        delegate.append(ansiControlCode)
        delegate.append(ansiReset)

        // pop the current tag from the stack
        currentSequenceStack.removeLast()

        // reapply all tags until that point, since the reset code resets everything
        currentSequenceStack.forEach {
            delegate.append(ansiControlCode)
            delegate.append(it.code)
        }
    }

    override fun toString(): String {
        return delegate.toString()
    }
}
