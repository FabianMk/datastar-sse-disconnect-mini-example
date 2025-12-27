(ns demo.system
  (:require [org.httpkit.server :as server]
            [demo.handler :as handler]))

(defonce server-state (atom nil))

(defn start-server!
  "Start the HTTP server"
  []
  (let [port 9001
        stop-fn (server/run-server #'handler/app {:port port})]
    (reset! server-state stop-fn)
    (println (str "Server started on http://localhost:" port))
    (println "Open http://localhost:9001/dashboard to see the issue")))

(defn stop-server!
  "Stop the HTTP server"
  []
  (when-let [stop-fn @server-state]
    (stop-fn)
    (reset! server-state nil)
    (println "Server stopped")))

(defn -main
  "Main entry point"
  [& _args]
  (start-server!))
