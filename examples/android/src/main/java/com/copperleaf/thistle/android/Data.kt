package com.copperleaf.thistle.android

object Data {

    val headerTextContextCounter = """
        |{{monospace}}
        |{{foreground color=context.counter}}count: {counterHex}{{/foreground}}
        |
        |{{foreground color=#000000}}{{decAlpha}}-alpha{{/decAlpha}} | {{incAlpha}}+alpha{{/incAlpha}}{{/foreground}}
        |{{foreground color=#FF0000}}{{decRed  }}-red  {{/decRed  }} | {{incRed  }}+red  {{/incRed  }}{{/foreground}}
        |{{foreground color=#00FF00}}{{decGreen}}-green{{/decGreen}} | {{incGreen}}+green{{/incGreen}}{{/foreground}}
        |{{foreground color=#0000FF}}{{decBlue }}-blue {{/decBlue }} | {{incBlue }}+blue {{/incBlue }}{{/foreground}}
        |{{/monospace}}
    """.trimMargin()

    val headerTextContextColor = "{{colorFromContext color=context.color}}color: {color}{{/colorFromContext}}"

    private val reallyComplexExample = """
        |this is text with a
        |{{monospace}}
        |  monospace,
        |  {{strikethrough}}
        |    strikethrough,
        |    {{underline}}
        |      underline,
        |      {{fgRed}}
        |        fgRed,
        |        {{bgGreen}}
        |          bgGreen, {{superscript}}superscript{{/superscript}}, {{subscript}}subscript{{/subscript}} text
        |        {{/bgGreen}}
        |      {{/fgRed}}
        |    {{/underline}}
        |  {{/strikethrough}}
        |{{/monospace}}
    """.trimMargin().replace("\\s+".toRegex(), " ")

    val inputs = listOf(
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
        reallyComplexExample,

        // examples from the "syntax" documentation
        "This text will be {{foreground color=#FF0000}}red{{/foreground}}, while this one has a " +
            "{{background color=#0000FF}}blue{{/background}} background.",
        "{{foreground color=#FF0000}}red{{/foreground}}",
        "{{foreground color=context.themeRed}}red{{/foreground}}",
        "Account: {{b}} {username} {{/b}} ({userId})",
        "{{toast}}Toast one{{/toast}} has different text than {{toast}}Toast 2 - ({counterHex}){{/toast}}.",
    )
}
