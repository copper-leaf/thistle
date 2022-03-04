package com.copperleaf.thistle.compose.android.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.copperleaf.thistle.compose.tags.ComposeLink
import com.copperleaf.thistle.compose.util.ProvideAdditionalThistleConfiguration
import com.copperleaf.thistle.compose.util.ProvideAdditionalThistleContext
import com.copperleaf.thistle.compose.util.StyledText
import com.copperleaf.thistle.compose.util.rememberStyledText
import kotlinx.coroutines.launch

@OptIn(ExperimentalStdlibApi::class)
@Suppress("UNCHECKED_CAST")
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

    ThistleConfiguration {
        Column {
            ProvideAdditionalThistleConfiguration({
                tag("inc") { ComposeLink(linkColor) { count += 10 } }
                tag("dec") { ComposeLink(linkColor) { count += -10 } }
            }) {
                StyledText(
                    text = "{{dec}}dec{{/dec}} | {{inc}}inc{{/inc}}",
                    onErrorDefaultTo = { it.message ?: "" },
                )
            }

            ProvideAdditionalThistleConfiguration({
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

                tag("toast") { ComposeLink { text -> coroutineScope.launch { snackbarHostState.showSnackbar(text) } } }
            }) {
                ProvideAdditionalThistleContext(
                    mapOf(
                        "counter" to count,
                        "counterHex" to count.toUInt().toString(16),
                        "color" to inputText.text.takeIf { it.isNotBlank() },

                        // context for examples from the "syntax" documentation
                        "themeRed" to Color.Red,
                        "username" to "AliceBob123",
                        "userId" to "123456789",
                    ).filterValues { it != null } as Map<String, Any>
                ) {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SnackbarHost(snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter).zIndex(10f))

                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(Modifier.padding(vertical = 16.dp)) {
                                Column(Modifier.weight(1f)) {
                                    Text("Counter", fontSize = 24.sp)

                                    StyledText(
                                        text = headerTextContextCounter,
                                        onErrorDefaultTo = { it.message ?: "" },
                                    )
                                }
                                Column(Modifier.weight(1f)) {
                                    Text("Input", fontSize = 24.sp)

                                    StyledText(
                                        text = headerTextContextColor,
                                        onErrorDefaultTo = { it.message ?: "" },
                                        noLinkClickedHandler = {
                                        },
                                    )
                                    TextField(
                                        inputText,
                                        { inputText = it },
                                        label = { Text("context.color") }
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
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .padding(vertical = 8.dp)
                                    ) {
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
                                                    if (showAst) {
                                                        Divider(Modifier.padding(vertical = 8.dp))
                                                        val styledTextAst = rememberStyledText(
                                                            input = item,
                                                            onErrorDefaultTo = { it.message ?: "" },
                                                        )
                                                        Text(styledTextAst.ast.toString())
                                                    }
                                                }

                                                Divider()

                                                Column(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .wrapContentHeight()
                                                        .padding(16.dp)
                                                ) {
                                                    StyledText(
                                                        text = item,
                                                        onErrorDefaultTo = { it.message ?: "" },
                                                        noLinkClickedHandler = {
                                                            coroutineScope.launch {
                                                                snackbarHostState.showSnackbar("Not handled")
                                                            }
                                                        },
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
