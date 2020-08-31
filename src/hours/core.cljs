(ns hours.core
  (:require
    [reagent.core :as r]
    [reagent.dom :as rdom]
    [cljs-time.core :as time]
    [cljs-time.format :as time-format]
    [hours.calendar :as cal]))

(defn week [week]
  [:div [:p (time-format/unparse (time-format/formatter "d/MM") (first (cal/days-of-week week)))]])

(defn month [for-day]
  (into [:div] (for [w (reverse (cal/weeks for-day))] (week w))))

(defn hours-app []
  [:div
    [:h1 "Hours"]
    (month (time/today))
    ])

(defn main! []
  (rdom/render [hours-app] (js/document.getElementById "app")))

(defn reload! []
  (main!)
  (prn "Reloaded"))
