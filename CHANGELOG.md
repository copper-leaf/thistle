## 1.2.0 - 2021-05-17

- Refactors `AndroidThistleRenderer` so that creating the actual Spans happens after recursion, to allow access to 
    their nested content and the View's `Context` when creating the Span.
- Changes signature of `ThistleTag` to accept a generic `ThistleRenderContext` instead of the context and args as 
    separate parameters. This requires the parsing and rendering context to match by the renderer's specific 
    RenderContext implementation and provide additional information for that render target.

## 1.1.0 - 2021-05-17

- Adds console renderer, improves APIs to better support multiple renderer types

## 1.0.0 - 2021-04-15

- Initial Commit
