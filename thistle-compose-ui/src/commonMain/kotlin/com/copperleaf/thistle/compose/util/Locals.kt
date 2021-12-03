package com.copperleaf.thistle.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.copperleaf.thistle.compose.ComposeDefaults
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleParser
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

@ExperimentalStdlibApi
typealias ComposeSyntaxBuilder =
    ThistleSyntaxBuilder<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeStyledText>

@ExperimentalStdlibApi
typealias ComposeSyntaxBuilderDefaults =
    ThistleSyntaxBuilder.Defaults<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeStyledText>

@ExperimentalStdlibApi
typealias ComposeThistleParser =
    ThistleParser<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeStyledText>

@ExperimentalStdlibApi
val LocalThistle = staticCompositionLocalOf<ComposeThistleParser> {
    error("CompositionLocal LocalThistle not present")
}

@ExperimentalStdlibApi
val LocalThistleContext = staticCompositionLocalOf<Map<String, Any>> {
    error("CompositionLocal LocalThistleContext not present")
}

@ExperimentalStdlibApi
private val LocalThistleDefaults = staticCompositionLocalOf<ComposeSyntaxBuilderDefaults> {
    error("CompositionLocal LocalThistleDefaults not present")
}

@ExperimentalStdlibApi
private val LocalThistleConfiguration = staticCompositionLocalOf<List<ComposeSyntaxBuilder.() -> Unit>> {
    error("CompositionLocal LocalThistleConfiguration not present")
}

/**
 * Provide a LocalThistle for [StyledText] composables.
 */
@ExperimentalStdlibApi
@Composable
fun ProvideThistle(
    defaults: ComposeSyntaxBuilderDefaults = ComposeDefaults(),
    additionalConfiguration: ComposeSyntaxBuilder.() -> Unit = {},
    context: Map<String, Any> = emptyMap(),
    content: @Composable () -> Unit,
) {
    val thistle = ThistleParser(defaults, additionalConfiguration)
    CompositionLocalProvider(
        LocalThistle provides thistle,
        LocalThistleDefaults provides defaults,
        LocalThistleContext provides context,
        LocalThistleConfiguration provides listOf(additionalConfiguration),
    ) {
        content()
    }
}

/**
 * Using the current configuration provided by [ProvideThistle], add new configuration tags to [content].
 */
@ExperimentalStdlibApi
@Composable
fun ProvideAdditionalThistleConfiguration(
    additionalConfiguration: ComposeSyntaxBuilder.() -> Unit,
    content: @Composable () -> Unit,
) {
    val currentThistleDefaults = LocalThistleDefaults.current
    val currentThistleConfiguration = LocalThistleConfiguration.current
    val newThistleConfiguration = currentThistleConfiguration + additionalConfiguration

    val newThistle = ThistleParser(currentThistleDefaults) {
        for (configuration in newThistleConfiguration) {
            configuration()
        }
    }
    CompositionLocalProvider(
        LocalThistle provides newThistle,
        LocalThistleConfiguration provides newThistleConfiguration,
    ) {
        content()
    }
}

/**
 * Using the current Thistle Context provided by [ProvideThistle], add new context values to [content].
 */
@ExperimentalStdlibApi
@Composable
fun ProvideAdditionalThistleContext(
    additionalContext: Map<String, Any>,
    content: @Composable () -> Unit,
) {
    val currentThistleContext = LocalThistleContext.current
    val newThistleContext = currentThistleContext + additionalContext

    CompositionLocalProvider(
        LocalThistleContext provides newThistleContext,
    ) {
        content()
    }
}
