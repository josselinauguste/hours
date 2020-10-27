(ns hours.views
  (:require
    [re-frame.core :as reframe]
    [cljs-time.core :as time]
    [cljs-time.format :as time-format]
    [hours.calendar]
    [hours.overtime]
    [hours.events]))

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
  (let [overtime @(reframe/subscribe [:get-overtime date])]
    [:p.inline-block.text-sm.text-center
    (day-name date)
    [:br]
    [:input.w-8.text-center {:on-change #(hours.events/dispatch-overtime-change-event date (.. % -target -value))
                             :value overtime
                             :type "number"
                             :pattern "-?[0-9]{1-3}"
                             :step 0
                             :maxLength 4}]]))

(defn week [week]
  (let [week-balance @(reframe/subscribe [:get-week-balance week])]
    [:div.mt-2
     [:div
      [:span.inline-block.align-middle (time-format/unparse (time-format/formatter "d/MM") (first (hours.calendar/days-of-week week)))]
      [:span.text-xs.text-gray-600.inline-block.m-3 (hours.overtime/format-balance week-balance)]]
     [:div.grid.grid-cols-5
      (for [d (hours.calendar/days-of-week week)]
        ^{:key d} [day d])]]))

(defn month [for-day]
  [:div.mt-4
   (for [w (reverse (hours.calendar/weeks for-day))]
     ^{:key w} [week w])])

(defn hours-app []
  [:div.container.p-4.mx-auto.md:max-w-lg.text-gray-900
    [:h1.mt-4.text-2xl "Hours"]
    (month (time/today))
    ])
