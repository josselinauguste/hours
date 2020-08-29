(ns hours.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]))

(defn hours-app []
  [:div
   [:h1 "Hours"]
   ])

(defn main! []
  (rdom/render [hours-app] (js/document.getElementById "app")))
