package com.copperleaf.thistle.app.ui.main

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import com.copperleaf.thistle.app.R
import com.copperleaf.thistle.app.databinding.MainFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalStdlibApi::class, ExperimentalTime::class, ExperimentalUnsignedTypes::class)
class MainFragment : Fragment() {

    private var binding: MainFragmentBinding? = null
    private val vm: MainViewModel by activityViewModels { MainViewModelFactory(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MainFragmentBinding
            .inflate(inflater, container, false)
            .also { binding = it }
            .apply {
                tvCounter.movementMethod = LinkMovementMethod.getInstance()
                tvColor.movementMethod = LinkMovementMethod.getInstance()

                etContextColor.doAfterTextChanged {
                    vm.updateColorInContext(it.toString())
                }

                cbShowAst.setOnClickListener {
                    vm.toggleShowAst()
                }

                rvExamples.addItemDecoration(SimpleDividerItemDecoration(requireActivity(), R.drawable.example_divider))
                rvExamples.adapter = ExampleAdapter(vm.state.value)
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            vm.state.collect { state ->
                binding?.applyState(state)
            }
        }
    }

    private fun MainFragmentBinding.applyState(state: MainViewModel.State) {
        // attempt to display the 'clickMe' text, which may throw an exception based on the user's input
        try {
            tvCounter.text = state.headerTextContextCounter.render(
                requireContext(),
                state.thistle,
                state.thistleContext
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            tvCounter.text = e.message
        }

        try {
            tvColor.text = state.headerTextContextColor.render(
                requireContext(),
                state.thistle,
                state.thistleContext
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            tvColor.text = e.message
        }

        // re-render all items in the adapter
        (rvExamples.adapter as? ExampleAdapter)?.onNewState(state)
    }
}
