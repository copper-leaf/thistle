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
