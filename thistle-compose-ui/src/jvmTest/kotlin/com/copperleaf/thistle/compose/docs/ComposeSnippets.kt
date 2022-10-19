package com.copperleaf.thistle.compose.docs

import androidx.compose.runtime.Composable
import com.copperleaf.thistle.compose.util.ProvideAdditionalThistleConfiguration
import com.copperleaf.thistle.compose.util.ProvideAdditionalThistleContext
import com.copperleaf.thistle.compose.util.ProvideThistle
import com.copperleaf.thistle.compose.util.StyledText

/**
 * The snippets in this file are not actually tested now, but they are used in documentation.
 *
 * TODO: setup Robolectric to actually test these
 */
/* ktlint-disable max-line-length */
@Suppress("UNUSED_ANONYMOUS_PARAMETER", "UNUSED_PARAMETER")
class ComposeSnippets {

    @Composable
    private fun MaterialTheme(block: @Composable () -> Unit) {
    }

    @Composable
    fun composePreview() {
        // snippet::compose-preview[compose,preview]
        MaterialTheme {
            ProvideThistle {
                StyledText("Text with {{b}}bold{{/b}} or {{foreground color=#ff0000}}red{{/foreground}} styles")
            }
        }
        // end::compose-preview
    }

    @Composable
    fun composeBasicUsage() {
        // snippet::compose-basic-usage[compose]
        // configure the Thistle parser at your UI root, right after the Theme. It can be updated or replaced further down the tree, if needed
        MaterialTheme {
            ProvideThistle {

                // Add additional tags to the root Thistle configuration for sub-trees of your UI. Especially useful for
                // configuring Link tags that are only relevant for a small section of UI, without having to define
                // those at the application root.
                //
                // This can be nested multiple times, but must be a child of `ProvideThistle`.
                ProvideAdditionalThistleConfiguration({
                    tag("newTag") { TODO() }
                }) {
                }

                // Add data to the Thistle Context variables for the UI sub-tree.
                //
                // This can be nested multiple times, but must be a child of `ProvideThistle`.
                ProvideAdditionalThistleContext(
                    mapOf("a" to "b")
                ) {
                }

                // Parse a formatted string to an AnnotatedString instance, and set that as the text of a BasicText
                // composable. The resulting AnnotatedString is cached both from the input String and the Context: changed
                // to the input String will be re-parsed and re-evaluated, while changes to the Context will only be
                // re-evaluated with the new context values.
                //
                // This will typically be used as a child of `ProvideThistle`, but you can manually provide the Thistle
                // instance and Context as function properties if needed, for more manual control.
                StyledText(
                    "This is a {{b}}very important{{/b}}, {{foreground color=#ff0000}}urgent{{/foreground}} message!"
                )
            }
        }
        // end::compose-basic-usage
    }
}
/* ktlint-enable max-line-length */
