(ns demo.html
  (:require [hiccup2.core :as h]))

(defn index
  "Renders the initial HTML shim with Datastar SDK"
  []
  (str (h/html {:mode :html}
                [:html
                 [:head
                  [:meta {:charset "utf-8"}]
                  [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
                  [:title "Datastar Connection Debug"]
                  ;; Load Datastar SDK v1.0.0-RC.7
                  [:script {:type "module"
                            :src "https://cdn.jsdelivr.net/gh/starfederation/datastar@v1.0.0-RC.7/bundles/datastar.js"}]
                  [:style "body { font-family: sans-serif; padding: 2rem; }
                           .game { padding: 1rem; margin: 0.5rem 0; border: 1px solid #ccc; }"]]
                 [:body
                  [:div#main
                   [:noscript "You need to enable JavaScript to run this app."]
                   ;; This triggers the POST request that starts the SSE stream
                   [:div {:data-init "@post('/dashboard')"}]]]])))

(defn shim-handler
  "Handler that returns the initial HTML shim"
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (index)})
