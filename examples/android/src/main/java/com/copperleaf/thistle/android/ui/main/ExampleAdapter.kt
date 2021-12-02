package com.copperleaf.thistle.android.ui.main

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.copperleaf.thistle.android.databinding.ThistleExampleBinding
import kotlin.time.ExperimentalTime

@ExperimentalStdlibApi
@ExperimentalTime
@ExperimentalUnsignedTypes
class ExampleAdapter(
    private var state: MainViewModel.State
) : RecyclerView.Adapter<ExampleAdapter.ViewHolder>() {

    fun onNewState(state: MainViewModel.State) {
        this.state = state
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ThistleExampleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(input: InputCache, state: MainViewModel.State) {
            binding.input.text = input.text
            binding.ast.text = input.astString
            binding.output.text = input.render(state.thistle, state.thistleContext)

            binding.astLayout.isVisible = state.showAst
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
        holder.bind(state.inputs[position], state)
    }

    override fun getItemCount(): Int {
        return state.inputs.size
    }
}
