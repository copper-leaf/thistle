package com.copperleaf.thistle.compose.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.copperleaf.thistle.compose.desktop.ui.main.MainContent

@OptIn(ExperimentalComposeUiApi::class, kotlin.ExperimentalStdlibApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 480.dp, height = 640.dp)
    ) {
        MaterialTheme {
            MainContent(
                headerTextContextCounter = Data.headerTextContextCounter,
                headerTextContextColor = Data.headerTextContextColor,
                inputs = Data.inputs,
            )
        }
    }
}
