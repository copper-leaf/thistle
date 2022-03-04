package com.copperleaf.thistle.compose.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.copperleaf.thistle.core.renderer.ThistleTagsArgs
import kotlin.properties.ReadOnlyProperty

@Suppress("NOTHING_TO_INLINE")
public inline fun ThistleTagsArgs.color(
    value: Color? = null,
    name: String? = null,
): ReadOnlyProperty<Nothing?, Color> = parameter<Any, Color>(
    value,
    name
) { _, v ->
    if (v is String) {
        Color.parse(v)
    } else if (v is Int) {
        Color(v)
    } else if (v is Color) {
        v
    } else {
        error("Color value must be an Int or String, got $v")
    }
}

public fun Color.Companion.parse(input: String): Color {
    val trimmed = input.trim()
    return parseColorAsName(trimmed)
        ?: parseColorAsHex(trimmed)
        ?: Color.Unspecified
}

public fun Color.serializeArgb(): String {
    val v = (0xFFFFFFFFu and this.toArgb().toUInt()).toString(16).padStart(8, '0')
    return "#$v"
}

public fun Color.serializeRgb(): String {
    val v = (0xFFFFFFu and this.toArgb().toUInt()).toString(16).padStart(6, '0')
    return "#$v"
}

private fun parseColorAsName(input: String): Color? {
    return when (input.lowercase()) {
        "black" -> Color.Black
        "blue" -> Color.Blue
        "cyan" -> Color.Cyan
        "darkgray" -> Color.DarkGray
        "gray" -> Color.Gray
        "green" -> Color.Green
        "lightgray" -> Color.LightGray
        "magenta" -> Color.Magenta
        "red" -> Color.Red
        "white" -> Color.White
        "yellow" -> Color.Yellow
        else -> null
    }
}

private fun parseColorAsHex(input: String): Color? {
    if (input.startsWith("#")) {
        if (input.length == 7) {
            val r = input.substring(1..2).toInt(16)
            val g = input.substring(3..4).toInt(16)
            val b = input.substring(5..6).toInt(16)
            return Color(r, g, b)
        } else if (input.length == 9) {
            val a = input.substring(1..2).toInt(16)
            val r = input.substring(3..4).toInt(16)
            val g = input.substring(5..6).toInt(16)
            val b = input.substring(7..8).toInt(16)
            return Color(r, g, b, a)
        }
    }

    return null
}
