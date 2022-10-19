package com.copperleaf.thistle.compose.android.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.thistle.compose.tags.ComposeBackgroundColor
import com.copperleaf.thistle.compose.tags.ComposeForegroundColor
import com.copperleaf.thistle.compose.tags.ComposeStyle
import com.copperleaf.thistle.compose.tags.ComposeTextDecoration
import com.copperleaf.thistle.compose.util.ProvideThistle
import com.copperleaf.thistle.core.asThistleValueParser

@Composable
fun ThistleConfiguration(
    content: @Composable () -> Unit
) {
    ProvideThistle(additionalConfiguration = {
        valueFormat {
            MappedParser(
                LiteralTokenParser("@color/red")
            ) { Color.Red }.asThistleValueParser()
        }

        tag("fgRed") { ComposeForegroundColor(Color.Red) }
        tag("fgGreen") { ComposeForegroundColor(Color.Green) }
        tag("fgBlue") { ComposeForegroundColor(Color.Blue) }

        tag("bgRed") { ComposeBackgroundColor(Color.Red) }
        tag("bgGreen") { ComposeBackgroundColor(Color.Green) }
        tag("bgBlue") { ComposeBackgroundColor(Color.Blue) }

        tag("bold") { ComposeStyle(hardcodedWeight = FontWeight.Bold, hardcodedStyle = FontStyle.Normal) }
        tag("italic") { ComposeStyle(hardcodedWeight = FontWeight.Normal, hardcodedStyle = FontStyle.Italic) }
        tag("underline") { ComposeTextDecoration(TextDecoration.Underline) }

        tag("colorFromContext") { ForegroundColorFromString() }
    }) {
        content()
    }
}
