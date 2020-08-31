(ns hours.calendar-test
  (:require [cljs.test :refer [deftest is]]
            [cljs-time.core :as time]
            [hours.calendar :as cal]))

(deftest get-weeks-for-a-date-month
  (let [expected [(time/local-date 2020 3 2) (time/local-date 2020 3 9) (time/local-date 2020 3 16) (time/local-date 2020 3 23) (time/local-date 2020 3 30)]
        actual (cal/weeks (time/local-date 2020 3 20))]
    (is (every? identity (map time/equal? expected actual)))))

(deftest get-days-for-a-week
  (let [expected [(time/local-date 2020 3 2) (time/local-date 2020 3 3) (time/local-date 2020 3 4) (time/local-date 2020 3 5) (time/local-date 2020 3 6)]
        actual (cal/days-of-week (time/local-date 2020 3 2))]
    (is (every? identity (map time/equal? expected actual)))))
