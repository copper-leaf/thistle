package com.copperleaf.thistle.console.ansi

internal data class AnsiStringComponent(
    val escapeCodes: List<AnsiEscapeCode>,
    val textContent: String,
) {
    fun renderComponent(): String {
        if (escapeCodes.isEmpty()) return textContent

        val startCodes = escapeCodes.joinToString(separator = "") { it.renderCode() }
        val endCode = AnsiEscapeCode.renderResetCode()
        return "$startCodes$textContent$endCode"
    }
}
