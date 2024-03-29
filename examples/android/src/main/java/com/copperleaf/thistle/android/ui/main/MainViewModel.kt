package com.copperleaf.thistle.android.ui.main

import android.text.Spanned
import android.util.Log
import androidx.lifecycle.ViewModel
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
class MainViewModel(initialState: State) : ViewModel() {

    data class State(
        val thistle: ThistleParser<AndroidThistleRenderContext, Any, Spanned>,
        val headerTextContextCounter: InputCache,
        val headerTextContextColor: InputCache,
        val inputs: List<InputCache>,
        val showAst: Boolean,
        val thistleContext: Map<String, Any>,
    ) {
        constructor(
            thistle: ThistleParser<AndroidThistleRenderContext, Any, Spanned>,
            headerTextContextCounter: String,
            headerTextContextColor: String,
            inputs: List<String>,
            showAst: Boolean,
            thistleContext: Map<String, Any>
        ) : this(
            thistle,
            headerTextContextCounter.parseFromThistle(thistle),
            headerTextContextColor.parseFromThistle(thistle),
            inputs.map { it.parseFromThistle(thistle) },
            showAst,
            thistleContext
        )

        companion object {
            @ExperimentalTime
            fun String.parseFromThistle(thistle: ThistleParser<AndroidThistleRenderContext, Any, Spanned>): InputCache {
                Log.i("MainViewModel", "parsing thistle string... [$this]")

                val inputCache: InputCache
                val duration = measureTime {
                    val rootNode = thistle.parse(ParserContext.fromString(this))
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
            val currentContext = it.thistleContext.toMutableMap()
            val currentCounterValue = currentContext["counter"] as? Int ?: 0
            val nextCounterValue = currentCounterValue + delta

            currentContext["counter"] = nextCounterValue
            currentContext["counterHex"] = nextCounterValue.toUInt().toString(16)
            it.copy(thistleContext = currentContext.toMap())
        }
    }

    fun updateColorInContext(color: String) {
        _state.value = _state.value.let {
            val currentContext = it.thistleContext.toMutableMap()

            if (color.isBlank()) {
                currentContext.remove("color")
            } else {
                currentContext["color"] = color
            }

            it.copy(thistleContext = currentContext.toMap())
        }
    }

    fun toggleShowAst() {
        _state.value = _state.value.let {
            it.copy(showAst = !it.showAst)
        }
    }
}
