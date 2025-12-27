(ns demo.handler
  (:require [reitit.ring :as ring]
            [demo.html :as html]
            [demo.dashboard :as dashboard]
            [demo.datastar :as ds]))

(def routes
  [["/dashboard" {:name :dashboard
                  :get {:handler html/shim-handler}
                  :post {:middleware [[ds/wrap-stream]]
                         :handler dashboard/mount}}]])

(def app
  (ring/ring-handler
   (ring/router routes)
   (ring/create-default-handler)))
