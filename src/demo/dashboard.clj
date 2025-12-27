(ns demo.dashboard
  (:require [demo.datastar :as ds]
            [hiccup2.core :as h]))

(defn render-games
  "Renders the games list as hiccup"
  [games]
  [:div#main
   [:h1 "Dashboard"]
   [:p "This demonstrates the Datastar SDK connection issue when morphing #main"]
   [:div.games
    (for [game games]
      [:div.game
       [:h3 (:name game)]
       [:p "Created: " (:created-at game)]])]])

(defn get-games
  "Simulates fetching games from a database"
  []
  [{:name "Game 1" :created-at "2025-12-26 10:00"}
   {:name "Game 2" :created-at "2025-12-26 11:30"}
   {:name "Game 3" :created-at "2025-12-26 14:15"}])

(defn mount
  "POST handler that establishes SSE stream and sends initial render"
  [_request sse]
  (println "Mounting dashboard, sending initial render via SSE")
  (let [games (get-games)
        hiccup (render-games games)]
    ;; This patch-elements! call with #main selector is what causes the connection to drop
    (ds/patch-elements! sse hiccup)
    (println "Initial render sent")))
