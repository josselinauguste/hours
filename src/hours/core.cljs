(ns hours.core
  (:require
    [reagent.core :as r]
    [reagent.dom :as rdom]
    [cljs-time.core :as time]
    [cljs-time.format :as time-format]
    [hours.calendar :as cal]))

(defn day [name]
  [:p.inline-block.text-sm.text-center name [:br] [:input.w-4.text-center {:type "text" :value "0"}]])

(defn week [week]
  [:div.mt-2
   [:p.block (time-format/unparse (time-format/formatter "d/MM") (first (cal/days-of-week week)))]
   [:div.grid.grid-cols-5
    (day "mon")
    (day "tue")
    (day "wed")
    (day "thu")
    (day "fri")]])

(defn month [for-day]
  (into [:div.mt-4] (for [w (reverse (cal/weeks for-day))] (week w))))

(defn hours-app []
  [:div.container.p-4.mx-auto.md:max-w-lg.text-gray-900
    [:h1.mt-4.text-2xl "Hours"]
    (month (time/today))
    ])

(defn main! []
  (rdom/render [hours-app] (js/document.getElementById "app")))

(defn reload! []
  (main!)
  (prn "Reloaded"))
