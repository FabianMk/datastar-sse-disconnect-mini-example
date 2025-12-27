(ns demo.datastar
  (:require [starfederation.datastar.clojure.api :as ds]
            [starfederation.datastar.clojure.adapter.http-kit :as ds-http-kit]
            [hiccup2.core :as h]))

(defn patch-elements!
  "Send a DOM patch to the client via SSE using Datastar SDK"
  [sse hiccup]
  (let [html (str (h/html hiccup))]
    ;; Using the SDK's patch-elements! function
    ;; This causes the connection to close when morphing #main
    (ds/patch-elements! sse html)))

(defn wrap-stream
  "Middleware that wraps a handler to create a long-running SSE stream using Datastar SDK"
  [handler]
  (fn [request]
    (ds-http-kit/->sse-response
      request
      {ds-http-kit/on-open (fn [sse]
                             (println "SSE connection opened")
                             ;; Call the handler which will use the sse channel
                             (handler request sse))
       ds-http-kit/on-close (fn [_sse _cause]
                              (println "SSE connection closed"))})))
