# Datastar SDK Connection Issue - Minimal Reproducible Example

This is a minimal reproducible example demonstrating an issue with the Datastar Clojure SDK (v1.0.0-RC7) when used with HTTP-Kit (v2.9.0-beta3).

## The Issue

When the Datastar SDK sends a `patch-fragments!` event that morphs the `#main` element (the root element that contains the `data-init` directive), the SSE connection appears to close or disconnect.

## Setup

This minimal example contains:
- **GET /dashboard**: Returns an HTML shim that loads Datastar SDK v1.0.0-RC.7 from CDN
- **POST /dashboard**: Establishes an SSE connection and sends initial render that morphs `#main`

## How to Run

1. Start the REPL:
```bash
cd ds-connection
clj
```

2. Load and start the system:
```clojure
(require '[demo.system :as sys])
(sys/start-server!)
```

3. Open your browser to: http://localhost:9001/dashboard

## Expected Behavior

1. Browser loads the HTML shim (GET /dashboard)
2. Datastar SDK executes `data-init="@post('/dashboard')"`
3. POST /dashboard creates SSE connection
4. Server sends `patch-fragments!` with `#main` selector and morph mode
5. Browser morphs the `#main` div with the rendered games list
6. SSE connection remains open

## Actual Behavior

1. ✅ Browser loads the HTML shim
2. ✅ Datastar SDK executes POST
3. ✅ SSE connection established
4. ✅ Server sends `patch-fragments!`
5. ✅ Browser morphs the `#main` div
6. ❌ **SSE connection closes/disconnects**

## Debugging

Check the browser console and network tab:
- You should see the SSE connection in Network tab
- Check if it closes after the initial patch
- Check console for any Datastar SDK errors

Check server logs:
- You should see "SSE connection opened"
- You should see "Mounting dashboard, sending initial render via SSE"
- You should see "Initial render sent"
- You may see "SSE connection closed" shortly after

## Key Files

- `src/demo/system.clj` - HTTP server setup
- `src/demo/handler.clj` - Router with GET/POST /dashboard endpoints
- `src/demo/html.clj` - HTML shim with Datastar SDK
- `src/demo/datastar.clj` - Datastar SDK wrapper (the problematic code)
- `src/demo/dashboard.clj` - Dashboard render logic

## The Problematic Code

In `src/demo/datastar.clj`:

```clojure
(defn patch-elements! [sse hiccup]
  (let [html (str (h/html hiccup))]
    (ds/patch-fragments! sse [{:selector "#main"        ; Morphing #main
                               :merge-mode "morph"       ; Using morph mode
                               :settle-duration 300
                               :fragments html
                               :use-view-transition false}])))
```

The issue appears to be related to morphing the `#main` element which is the root element that contains the `data-init` directive that established the SSE connection.

## Workaround Attempts

In the main project, we tried:
1. Using `merge-mode "inner"` instead of `"outer"` - still had issues
2. Implementing manual SSE without the SDK - this worked but lost SDK features
3. Changing the selector to a child element - worked but requires restructuring

## Environment

- Clojure: 1.12.0
- HTTP-Kit: 2.9.0-beta3
- Datastar Clojure SDK: 1.0.0-RC7
- Datastar JS (CDN): v1.0.0-RC.7
- Reitit: 0.7.2
- Hiccup: 2.0.0-RC3
