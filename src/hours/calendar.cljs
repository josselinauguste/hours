(ns hours.calendar
  (:require [cljs-time.core :as time]))

(defn weeks [_date]
  [(time/local-date 2020 3 2) (time/local-date 2020 3 9) (time/local-date 2020 3 16) (time/local-date 2020 3 23) (time/local-date 2020 3 30)])

(defn days_of_week [week]
  [week])
