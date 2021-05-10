package com.copperleaf.thistle.app.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.parser.ThistleParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalStdlibApi
@ExperimentalTime
class MainViewModel(initialState: MainViewModel.State) : ViewModel() {

    data class State(
        val thistle: ThistleParser,
        val headerText: InputCache,
        val inputs: List<InputCache>,
        val counter: Int,
        val showAst: Boolean,
        val thistleContext: Map<String, Any>,
    ) {
        constructor(
            thistle: ThistleParser,
            headerText: String,
            inputs: List<String>,
            counter: Int,
            showAst: Boolean,
            thistleContext: Map<String, Any>
        ) : this(
            thistle,
            headerText.let { it.parseFromThistle(thistle) },
            inputs.map { it.parseFromThistle(thistle) },
            counter,
            showAst,
            thistleContext
        )

        companion object {
            @ExperimentalTime
            fun String.parseFromThistle(thistle: ThistleParser): InputCache {
                Log.i("MainViewModel", "parsing thistle string... [$this]")

                val inputCache: InputCache
                val duration = measureTime {
                    val (rootNode, _) = thistle.parser.parse(ParserContext.fromString(this))
                    val rootNodeAst = rootNode.toString()
                    inputCache = InputCache(
                        this,
                        rootNode,
                        rootNodeAst
                    )
                }

                Log.i("MainViewModel", "parsing thistle string -> $duration")

                return inputCache
            }
        }
    }

    private val _state = MutableStateFlow(initialState)
    val state get() = _state.asStateFlow()

    fun updateCounter(delta: Int) {
        _state.value = _state.value.let {
            it.copy(counter = it.counter + delta)
        }
    }

    fun updateColorInContext(color: String) {
        _state.value = _state.value.let {
            val currentContext = it.thistleContext.toMutableMap()
            currentContext["color"] = color
            it.copy(thistleContext = currentContext.toMap())
        }
    }

    fun toggleShowAst() {
        _state.value = _state.value.let {
            it.copy(showAst = !it.showAst)
        }
    }

}
