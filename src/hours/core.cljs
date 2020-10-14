(ns hours.core
  (:require
    [reagent.dom]
    [re-frame.core :as reframe]
    [hours.subs]
    [hours.views]))

(defn mount-ui
  []
  (reagent.dom/render [hours.views/hours-app] (js/document.getElementById "app")))

(defn main! []
  (reframe/dispatch-sync [:initialize])
  (mount-ui))

(defn reload! []
  (mount-ui)
  (prn "Reloaded"))
