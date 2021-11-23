package com.copperleaf.thistle.app.ui.main

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spanned
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.thistle.android.AndroidDefaults
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.android.tags.AndroidBackgroundColor
import com.copperleaf.thistle.android.tags.AndroidForegroundColor
import com.copperleaf.thistle.android.tags.AndroidStyle
import com.copperleaf.thistle.android.tags.Icon
import com.copperleaf.thistle.android.tags.Link
import com.copperleaf.thistle.android.tags.Underline
import com.copperleaf.thistle.app.Data
import com.copperleaf.thistle.app.R
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.parser.ThistleParser
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Suppress("UNCHECKED_CAST")
@ExperimentalStdlibApi
@ExperimentalTime
@ExperimentalUnsignedTypes
class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private lateinit var vm: MainViewModel

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val thistle: ThistleParser<AndroidThistleRenderContext, Any, Spanned>

        Log.i("MainViewModelFactory", "creating thistle...")
        val duration = measureTime {
            thistle = ThistleParser(AndroidDefaults(context, "com.copperleaf.thistle.app")) {
                valueFormat {
                    MappedParser(
                        LiteralTokenParser("@color/red")
                    ) { Color.RED }.asThistleValueParser()
                }

                tag("fgRed") { AndroidForegroundColor(Color.RED) }
                tag("fgGreen") { AndroidForegroundColor(Color.GREEN) }
                tag("fgBlue") { AndroidForegroundColor(Color.BLUE) }

                tag("bgRed") { AndroidBackgroundColor(Color.RED) }
                tag("bgGreen") { AndroidBackgroundColor(Color.GREEN) }
                tag("bgBlue") { AndroidBackgroundColor(Color.BLUE) }

                tag("bold") { AndroidStyle(Typeface.BOLD) }
                tag("italic") { AndroidStyle(Typeface.ITALIC) }
                tag("underline") { Underline() }

                tag("androidIcon") { Icon(ContextCompat.getDrawable(context, R.drawable.ic_android)) }

                tag("incAlpha") { Link { vm.updateCounter(0x08__00_00_00) } }
                tag("decAlpha") { Link { vm.updateCounter(-0x08__00_00_00) } }
                tag("incRed") { Link { vm.updateCounter(0x00__08_00_00) } }
                tag("decRed") { Link { vm.updateCounter(-0x00__08_00_00) } }
                tag("incGreen") { Link { vm.updateCounter(0x00__00_08_00) } }
                tag("decGreen") { Link { vm.updateCounter(-0x00__00_08_00) } }
                tag("incBlue") { Link { vm.updateCounter(0x00__00_00_08) } }
                tag("decBlue") { Link { vm.updateCounter(-0x00__00_00_08) } }

                tag("inc") { Link { vm.updateCounter(1) } }
                tag("dec") { Link { vm.updateCounter(-1) } }

                tag("colorFromContext") { ForegroundColorFromString() }

                tag("toast") { OnClickDisplayInnerContent() }
            }
        }
        Log.i("MainViewModelFactory", "creating thistle -> $duration")

        val initialState = MainViewModel.State(
            thistle = thistle,
            headerTextContextCounter = Data.headerTextContextCounter,
            headerTextContextColor = Data.headerTextContextColor,
            inputs = Data.inputs,
            showAst = false,
            thistleContext = mapOf(
                "counter" to 0xFF000000u.toInt(),
                "counterHex" to 0xFF000000u.toString(16),

                // context for examples from the "syntax" documentation
                "themeRed" to Color.RED,
                "username" to "AliceBob123",
                "userId" to "123456789",
            )
        )

        vm = MainViewModel(initialState)

        return vm as T
    }
}
