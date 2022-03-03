package com.copperleaf.thistle.compose.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.copperleaf.thistle.compose.desktop.ui.main.MainContent

@OptIn(ExperimentalComposeUiApi::class, kotlin.ExperimentalStdlibApi::class)
fun main() = singleWindowApplication(
    title = "Compose for Desktop",
    state = WindowState(width = 480.dp, height = 640.dp)
) {
    MaterialTheme {
        MainContent(
            headerTextContextCounter = Data.headerTextContextCounter,
            headerTextContextColor = Data.headerTextContextColor,
            inputs = Data.inputs,
        )
    }
}
