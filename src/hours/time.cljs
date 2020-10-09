(ns hours.time
  (:require
   [clojure.string]
   [hours.calendar :as cal]))

(defn format-balance [balance]
  (if (>= balance 0)
    (clojure.string/join "" ["+" balance "h"])
    (clojure.string/join "" ["-" (- 0 balance) "h"])))

(defn get-week-balance [get-day week]
  (let [week-days (cal/days-of-week week)]
    (apply + (map #(js/parseFloat (get-day %)) week-days))))
