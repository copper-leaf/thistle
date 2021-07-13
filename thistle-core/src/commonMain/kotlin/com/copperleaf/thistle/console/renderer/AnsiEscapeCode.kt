package com.copperleaf.thistle.console.renderer

data class AnsiEscapeCode(
    val code: String
) {
    fun renderCode(): String {
        return "${ansiControlCode}$code"
    }

    fun renderResetCode(): String {
        return "${ansiControlCode}$ansiReset"
    }

    companion object {
        internal const val ansiControlCode = '\u001B'
        internal const val ansiReset = "[0m"
    }
}
