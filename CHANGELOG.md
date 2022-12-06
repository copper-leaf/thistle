## 4.0.1 - 2022-12-06

- Fix an issue where `thistle-android` and `thistle-compose-ui` artifacts both generated the same Android `BuildConfig`
  class, causing an issue when both libraries are used in the same app.

## 4.0.0 - 2022-10-17

- Updates Kotlin version to 1.7.20

## 3.1.0 - 2022-03-02

- Updates Kotlin version to 1.6.10
- Enables ExplicitApi and BinaryCompatibilityValidator on the repo, to improve the stability of its API
- [`thistle-compose-ui`] Adds a `noLinkClickedHandler` callback to `StyledText`, to help propagate clicks that do not
  fall onto clickable regions of text.

## 2.0.2 - 2021-12-06

- Use identifier instead of letter/number parser for interpolated value keys
- Fix issue displaying inline content in Compose renderer

## 2.0.1 - 2021-12-03

- Fix publishing

## 2.0.0 - 2021-12-03

- Adds Compose target
- Splits Android and Console targets into separate artifacts
- Other minor refactoring of core APIs (some class names/packages may be slightly different)

## 1.4.0 - 2021-07-15

- Make tag names dynamic and checked against configured tags to ensure no tag is left unparsed, and undeclared tags throw and error
- Renames a couple classes to prevent ambiguity:
    - `ThistleTag` is now `ThistleTagFactory`
    - `ThistleTagBuilder` is now `ThistleTagConfiguration`

## 1.3.0 - 2021-07-14

- Rewrites Console renderer builder to use an intermediary tree structure to collect nested ASNI codes, rather than 
    managing a local stack. This allows the tags for the console to have access to their own content during creation, 
    like with the Android renderer.

## 1.2.0 - 2021-07-13

- Refactors `AndroidThistleRenderer` so that creating the actual Spans happens after recursion, to allow access to 
    their nested content and the View's `Context` when creating the Span.
- Changes signature of `ThistleTag` to accept a generic `ThistleRenderContext` instead of the context and args as 
    separate parameters. This requires the parsing and rendering context to match by the renderer's specific 
    RenderContext implementation and provide additional information for that render target.

## 1.1.0 - 2021-05-17

- Adds console renderer, improves APIs to better support multiple renderer types

## 1.0.0 - 2021-04-15

- Initial Commit
