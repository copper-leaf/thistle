package com.copperleaf.thistle.compose.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.copperleaf.thistle.compose.ComposeDefaults
import com.copperleaf.thistle.compose.detectRichTextClicks
import com.copperleaf.thistle.compose.rememberRichText
import com.copperleaf.thistle.compose.tags.ComposeInlineContent
import com.copperleaf.thistle.compose.tags.ComposeLink
import com.copperleaf.thistle.core.parser.ThistleParser

@OptIn(ExperimentalComposeUiApi::class, kotlin.ExperimentalStdlibApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        val initialText = "{{b}}bold{{/b}}, {count} clicks. {{inc}}++{{/inc}}{{div}}{{/div}}{{dec}}--{{/dec}}"

        var count by remember { mutableStateOf(0) }
        var inputText by remember { mutableStateOf(TextFieldValue(initialText)) }

        MaterialTheme {
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        count++
                    }
                ) {
                    Text(if (count == 0) "Hello World" else "Clicked $count!")
                }
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        count = 0
                    }
                ) {
                    Text("Reset")
                }
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it }
                )

                val linkColor = MaterialTheme.colors.primary

                val thistle = ThistleParser(ComposeDefaults) {
                    tag("inc") { ComposeLink(linkColor) { count++ } }
                    tag("dec") { ComposeLink(linkColor) { count-- } }
                    tag("div") {
                        ComposeInlineContent(
                            InlineTextContent(
                                // Placeholder tells text layout the expected size and vertical alignment of
                                // children composable.
                                Placeholder(
                                    width = 0.15.em,
                                    height = 0.90.em,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .rotate(15f)
                                        .fillMaxSize()
                                        .clip(RectangleShape)
                                        .background(Color.DarkGray)
                                )
                            }
                        )
                    }
                }
                val richText = rememberRichText(
                    thistle,
                    inputText.text,
                    context = mapOf(
                        "count" to count,
                    )
                ) { inputText.text }

                var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
                Text(
                    text = richText.result,
                    modifier = Modifier.detectRichTextClicks(layoutResult, richText),
                    inlineContent = richText.inlineContent,
                    onTextLayout = { layoutResult = it }
                )
            }
        }
    }
}
