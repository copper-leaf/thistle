package com.copperleaf.thistle.app.ui.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.thistle.DefaultAndroidTags
import com.copperleaf.thistle.app.R
import com.copperleaf.thistle.app.databinding.MainFragmentBinding
import com.copperleaf.thistle.app.databinding.ThistleExampleBinding
import com.copperleaf.thistle.applyStyledText
import com.copperleaf.thistle.parser.ThistleParser
import com.copperleaf.thistle.parser.ThistleSyntax
import com.copperleaf.thistle.renderer
import com.copperleaf.thistle.tags.BackgroundColor
import com.copperleaf.thistle.tags.ForegroundColor
import com.copperleaf.thistle.tags.Link
import com.copperleaf.thistle.tags.Style
import com.copperleaf.thistle.tags.Underline

@OptIn(ExperimentalStdlibApi::class)
class MainFragment : Fragment() {

    private var count: Int = 0
    private var showAst: Boolean = false

    private lateinit var thistle: ThistleParser
    private var binding: MainFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MainFragmentBinding
            .inflate(inflater, container, false)
            .also { binding = it }
            .apply {
                thistle = ThistleParser(
                    ThistleSyntax.builder {
                        from(DefaultAndroidTags)

                        literalFormat {
                            MappedParser(
                                LiteralTokenParser("@color/red")
                            ) { Color.RED }
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

                        tag("inc") { Link(updateCount(1)) }
                        tag("dec") { Link(updateCount(-1)) }
                    }
                )

                clickMe.applyStyledText(
                    thistle,
                    "{{inc}}Click Me!{{/inc}}    {{foreground color=@color/red}}|{{/foreground}}    {{dec}}Don't Click Me!{{/dec}}"
                )

                cbShowAst.setOnClickListener {
                    showAst = !showAst
                    cbShowAst.isChecked = showAst
                    updateAdapter()
                }

                setupAdapter()
                updateAdapter()
            }
            .root
    }

    private fun setupAdapter() {
        val adapter = ExampleAdapter()
        val decoration = SimpleDividerItemDecoration(requireActivity(), R.drawable.example_divider)
        binding!!.apply {
            rvExamples.addItemDecoration(decoration)
            rvExamples.adapter = adapter
        }
    }

    private fun updateAdapter() {
        requireActivity().runOnUiThread {
            binding!!.apply {
                rvExamples.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun updateCount(delta: Int): (View) -> Unit {
        return {
            requireActivity().runOnUiThread {
                count += delta
                binding?.tvCounter?.text = "count: $count"
            }
        }
    }

    data class InputCache(
        val text: String,
        val astString: String,
        val span: Spanned,
    )

    inner class ExampleAdapter : RecyclerView.Adapter<ExampleAdapter.ViewHolder>() {

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
        ).map { input ->
            Log.i("ExampleAdapter", input)
            val (rootNode, _) = thistle.parser.parse(ParserContext.fromString(input))
            val rootNodeAst = rootNode.toString()
            val rendered = thistle.renderer.render(rootNode)

            InputCache(
                input,
                rootNodeAst,
                rendered
            )
        }

        inner class ViewHolder(
            private val binding: ThistleExampleBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(input: InputCache) {
                binding.input.text = input.text
                binding.ast.text = input.astString
                binding.output.text = input.span

                binding.astLayout.isVisible = showAst
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ThistleExampleBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                    .apply {
                        input.movementMethod = LinkMovementMethod.getInstance()
                        ast.movementMethod = LinkMovementMethod.getInstance()
                        output.movementMethod = LinkMovementMethod.getInstance()
                    }
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(inputs[position])
        }

        override fun getItemCount(): Int {
            return inputs.size
        }
    }

    class SimpleDividerItemDecoration(context: Context, @DrawableRes dividerRes: Int) : RecyclerView.ItemDecoration() {

        private val mDivider: Drawable = ContextCompat.getDrawable(context, dividerRes)!!

        override fun onDrawOver(c: Canvas, parent: RecyclerView) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child: View = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top: Int = child.bottom + params.bottomMargin
                val bottom = top + mDivider.intrinsicHeight
                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }
}
