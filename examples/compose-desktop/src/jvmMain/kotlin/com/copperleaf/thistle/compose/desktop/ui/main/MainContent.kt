package com.copperleaf.thistle.compose.desktop.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.thistle.compose.ComposeDefaults
import com.copperleaf.thistle.compose.RichText
import com.copperleaf.thistle.compose.rememberRichText
import com.copperleaf.thistle.compose.tags.ComposeBackgroundColor
import com.copperleaf.thistle.compose.tags.ComposeForegroundColor
import com.copperleaf.thistle.compose.tags.ComposeLink
import com.copperleaf.thistle.compose.tags.ComposeStyle
import com.copperleaf.thistle.compose.tags.ComposeTextDecoration
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.parser.ThistleParser
import kotlinx.coroutines.launch

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun MainContent(
    headerTextContextCounter: String,
    headerTextContextColor: String,
    inputs: List<String>,
) {
    var count: Int by remember { mutableStateOf(0xFF000000u.toInt()) }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var showAst by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val linkColor = MaterialTheme.colors.primary
    val thistle = ThistleParser(ComposeDefaults) {
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

//        tag("androidIcon") { Icon(ContextCompat.getDrawable(context, R.drawable.ic_android)) }

        tag("incAlpha") { ComposeLink { count += 0x08__00_00_00 } }
        tag("decAlpha") { ComposeLink { count += -0x08__00_00_00 } }
        tag("incRed") { ComposeLink(Color.Red) { count += 0x00__08_00_00 } }
        tag("decRed") { ComposeLink(Color.Red) { count += -0x00__08_00_00 } }
        tag("incGreen") { ComposeLink(Color.Green) { count += 0x00__00_08_00 } }
        tag("decGreen") { ComposeLink(Color.Green) { count += -0x00__00_08_00 } }
        tag("incBlue") { ComposeLink(Color.Blue) { count += 0x00__00_00_08 } }
        tag("decBlue") { ComposeLink(Color.Blue) { count += -0x00__00_00_08 } }

        tag("inc") { ComposeLink(linkColor) { count += 1 } }
        tag("dec") { ComposeLink(linkColor) { count += -1 } }

        tag("colorFromContext") { ForegroundColorFromString() }

        tag("toast") { ComposeLink { text -> coroutineScope.launch { snackbarHostState.showSnackbar(text) } } }
    }

    val thistleContext by derivedStateOf {
        mapOf(
            "counter" to count,
            "counterHex" to count.toUInt().toString(16),
            "color" to inputText.text.takeIf { it.isNotBlank() },

            // context for examples from the "syntax" documentation
            "themeRed" to Color.Red,
            "username" to "AliceBob123",
            "userId" to "123456789",
        ).filterValues { it != null } as Map<String, Any>
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SnackbarHost(snackbarHostState)
        Row(Modifier.padding(vertical = 16.dp)) {
            Column(Modifier.weight(1f)) {
                Text("Counter", fontSize = 24.sp)

                RichText(
                    text = headerTextContextCounter,
                    thistle = thistle,
                    context = thistleContext,
                    onErrorDefaultTo = { it.message ?: "" },
                )
            }
            Column(Modifier.weight(1f)) {
                Text("Input", fontSize = 24.sp)

                RichText(
                    text = headerTextContextColor,
                    thistle = thistle,
                    context = thistleContext,
                    onErrorDefaultTo = { it.message ?: "" },
                )
                TextField(
                    inputText,
                    { inputText = it },
                    label = { Text("context.color")}
                )
            }
        }

        Column(Modifier.padding(vertical = 16.dp)) {
            Text("Options", fontSize = 24.sp)
            Row(Modifier) {
                Checkbox(showAst, { showAst = it })
                Text("Show AST")
            }
        }

        LazyColumn(
            modifier = Modifier.padding(top = 16.dp),
        ) {
            itemsIndexed(inputs) { _, item ->
                Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = 8.dp)) {
                    Card(
                        elevation = 8.dp,
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.25f))
                                    .clip(RoundedCornerShape(4.dp))
                                    .padding(16.dp)
                            ) {
                                Text(item)
                                if(showAst) {
                                    Divider(Modifier.padding(vertical = 8.dp))
                                    val richTextAst = rememberRichText(
                                        thistle,
                                        item,
                                        context = thistleContext,
                                        onErrorDefaultTo = { it.message ?: "" },
                                    )
                                    Text(richTextAst.ast.toString())
                                }
                            }

                            Divider()

                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(16.dp)
                            ) {
                                RichText(
                                    text = item,
                                    thistle = thistle,
                                    context = thistleContext,
                                    onErrorDefaultTo = { it.message ?: "" },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
