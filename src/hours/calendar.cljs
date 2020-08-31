(ns hours.calendar
  (:require [cljs-time.core :as time]))

(defn days-of-month [date]
  (loop [next-date (time/first-day-of-the-month date) days []]
    (if (> (time/month next-date) (time/month date))
      (reverse days)
      (recur (time/plus next-date (time/days 1)) (cons next-date days)))))

(defn weeks [date]
  (filter #(= 1 (time/day-of-week %)) (days-of-month date)))

(defn days-of-week [week]
  (map #(time/plus week (time/days %)) (range 5)))
