package com.copperleaf.thistle.app.ui.main

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.thistle.android.AndroidDefaults
import com.copperleaf.thistle.app.R
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.parser.ThistleParser
import com.copperleaf.thistle.android.tags.BackgroundColor
import com.copperleaf.thistle.android.tags.ForegroundColor
import com.copperleaf.thistle.android.tags.Icon
import com.copperleaf.thistle.android.tags.Link
import com.copperleaf.thistle.android.tags.Style
import com.copperleaf.thistle.android.tags.Underline
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Suppress("UNCHECKED_CAST")
@ExperimentalStdlibApi
@ExperimentalTime
@ExperimentalUnsignedTypes
class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

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

                from(AndroidDefaults(context, "com.copperleaf.thistle.app"))
            }
        }
        Log.i("MainViewModelFactory", "creating thistle -> $duration")

        val initialState = MainViewModel.State(
            thistle = thistle,
            /* ktlint-disable max-line-length */
            headerTextContextCounter = """
                |{{monospace}}
                |{{foreground color=context.counter}}count: {counterHex}{{/foreground}}
                |
                |{{foreground color=#000000}}{{decAlpha}}-alpha{{/decAlpha}} | {{incAlpha}}+alpha{{/incAlpha}}{{/foreground}}
                |{{foreground color=#FF0000}}{{decRed  }}-red  {{/decRed  }} | {{incRed  }}+red  {{/incRed  }}{{/foreground}}
                |{{foreground color=#00FF00}}{{decGreen}}-green{{/decGreen}} | {{incGreen}}+green{{/incGreen}}{{/foreground}}
                |{{foreground color=#0000FF}}{{decBlue }}-blue {{/decBlue }} | {{incBlue }}+blue {{/incBlue }}{{/foreground}}
                |{{/monospace}}
                """.trimMargin(),
            headerTextContextColor = """
                |{{colorFromContext color=context.color}}color: {color}{{/colorFromContext}}
                """.trimMargin(),
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

                "this is an {{icon drawable=@drawable/ic_android}}icon{{/icon}}",
                "this is an {{androidIcon}}androidIcon{{/androidIcon}}",

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

                // examples from the "syntax" documentation
                "This text will be {{foreground color=#FF0000}}red{{/foreground}}, while this one has a {{background color=#0000FF}}blue{{/background}} background.",
                "{{foreground color=#FF0000}}red{{/foreground}}",
                "{{foreground color=context.themeRed}}red{{/foreground}}",
                "Account: {{b}} {username} {{/b}} ({userId})",
            ),
            /* ktlint-enable max-line-length */
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
