(ns hours.core
  (:require
    [reagent.core :as r]
    [reagent.dom :as rdom]
    [cljs-time.core :as time]
    [cljs-time.format :as time-format]
    [hours.calendar :as cal]))

(defonce overtimes (r/atom {}))

(defn day-name [day]
  (case (time/day-of-week day)
    0 "sun"
    1 "mon"
    2 "tue"
    3 "wed"
    4 "thu"
    5 "fri"
    6 "sat"))

(defn day [date]
  [:p.inline-block.text-sm.text-center
   (day-name date)
   [:br]
   [:input.w-4.text-center {:on-change #(swap! overtimes assoc date (.. % -target -value))
                            :type "text"
                            :value (or (@overtimes date) "0")}]])

(defn week [week]
  [:div.mt-2
   [:p.block (time-format/unparse (time-format/formatter "d/MM") (first (cal/days-of-week week)))]
   [:div.grid.grid-cols-5
    (for [d (cal/days-of-week week)]
      ^{:key d} [day d])]])

(defn month [for-day]
  [:div.mt-4
   (for [w (reverse (cal/weeks for-day))]
     ^{:key w} [week w])])

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
