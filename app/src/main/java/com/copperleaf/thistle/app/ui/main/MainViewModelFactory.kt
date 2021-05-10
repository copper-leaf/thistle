package com.copperleaf.thistle.app.ui.main

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.thistle.DefaultAndroidTags
import com.copperleaf.thistle.asThistleValueParser
import com.copperleaf.thistle.parser.ThistleParser
import com.copperleaf.thistle.tags.BackgroundColor
import com.copperleaf.thistle.tags.ForegroundColor
import com.copperleaf.thistle.tags.Link
import com.copperleaf.thistle.tags.Style
import com.copperleaf.thistle.tags.Underline
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalStdlibApi
@ExperimentalTime
class MainViewModelFactory : ViewModelProvider.Factory {

    private lateinit var vm: MainViewModel

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val thistle: ThistleParser

        Log.i("MainViewModelFactory", "creating thistle...")
        val duration = measureTime {
            thistle = ThistleParser {
                valueFormat {
                    MappedParser(
                        LiteralTokenParser("@color/red")
                    ) { Color.RED }.asThistleValueParser()
                }

                tag("fgRed") { ForegroundColor(Color.RED) }
                tag("fgGreen") { ForegroundColor(Color.GREEN) }
                tag("fgBlue") { ForegroundColor(Color.BLUE) }

                tag("bgRed") { BackgroundColor(Color.RED) }
                tag("bgGreen") { BackgroundColor(Color.GREEN) }
                tag("bgBlue") { BackgroundColor(Color.BLUE) }

                tag("bold") { Style(Typeface.BOLD) }
                tag("italic") { Style(Typeface.ITALIC) }
                tag("underline") { Underline() }

                tag("inc") { Link { vm.updateCounter(1) } }
                tag("dec") { Link { vm.updateCounter(-1) } }

                tag("colorFromContext") { ForegroundColorFromString() }

                from(DefaultAndroidTags)
            }
        }
        Log.i("MainViewModelFactory", "creating thistle -> $duration")

        val initialState = MainViewModel.State(
            thistle = thistle,
            headerText = "{{foreground color=@color/red}}{{inc}}Click Me!{{/inc}}{{/foreground}}    |    {{colorFromContext color=context.color}}{{dec}}Don't Click Me!{{/dec}}{{/colorFromContext}}",
            inputs = listOf(
                "this has a {{bgRed}}red{{/bgRed}} background",
                "this has a {{bgGreen}}green{{/bgGreen}} background",
                "this has a {{bgBlue}}blue{{/bgBlue}} background",
                "this has a {{background color=#00FFFF}}#00FFFF{{/background}} background",
                "this has a {{background color=#FF00FF}}#FF00FF{{/background}} background",

                "this has {{fgRed}}red{{/fgRed}} text",
                "this has {{fgGreen}}green{{/fgGreen}} text",
                "this has {{fgBlue}}blue{{/fgBlue}} text",
                "this has {{foreground color=#00FFFF}}#00FFFF{{/foreground}} text",
                "this has {{foreground color=#FF00FF}}#FF00FF{{/foreground}} text",

                "this text is in {{bold}}bold{{/bold}} style",
                "this text is in {{b}}bold{{/b}} style",
                "this text is in {{italic}}italic{{/italic}} style",
                "this text is in {{i}}italic{{/i}} style",
                "this text is in {{style style=\"bold\"}}bold{{/style}} style",
                "this text is in {{style style=italic}}italic{{/style}} style",

                "this is an {{inc}}increment{{/inc}} link",
                "this is an {{dec}}decrement{{/dec}} link",
                "this is an {{url url=\"https://www.example.com/\"}}www.example.com{{/url}} link",

                "this is {{monospace}}monospace{{/monospace}} text",
                "this is {{sans}}sans-serif{{/sans}} text",
                "this is {{serif}}serif{{/serif}} text",
                "this is {{typeface typeface=\"monospace\"}}monospace{{/typeface}} text",
                "this is {{typeface typeface=sans}}sans{{/typeface}} text",
                "this is {{typeface typeface=serif}}serif{{/typeface}} text",

                "this text has a {{strikethrough}}strikethrough{{/strikethrough}}",
                "this text has an {{underline}}underline{{/underline}}",
                "this text has an {{u}}underline{{/u}}",

                "this is text with a {{subscript}}subscript{{/subscript}}",
                "this is text with a {{superscript}}superscript{{/superscript}}",
                """
                    |this is text with a
                    |{{monospace}}
                    |    monospace,
                    |    {{strikethrough}}
                    |        strikethrough,
                    |        {{underline}}
                    |            underline,
                    |            {{fgRed}}
                    |                fgRed,
                    |                {{bgGreen}}
                    |                    bgGreen, {{superscript}}superscript{{/superscript}}, {{subscript}}subscript{{/subscript}} text
                    |                {{/bgGreen}}
                    |            {{/fgRed}}
                    |        {{/underline}}
                    |    {{/strikethrough}}
                    |{{/monospace}}
                    """.trimMargin().replace("\\s+".toRegex(), " "),
            ),
            counter = 0,
            showAst = false,
            thistleContext = emptyMap()
        )

        vm = MainViewModel(initialState)

        return vm as T
    }
}
