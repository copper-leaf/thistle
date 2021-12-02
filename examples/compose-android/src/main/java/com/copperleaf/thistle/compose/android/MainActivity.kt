package com.copperleaf.thistle.compose.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.copperleaf.thistle.compose.android.ui.main.MainContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent(
                headerTextContextCounter = Data.headerTextContextCounter,
                headerTextContextColor = Data.headerTextContextColor,
                inputs = Data.inputs,
            )
        }
    }
}
