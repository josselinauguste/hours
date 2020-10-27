(ns hours.overtime-test
  (:require [cljs.test :refer [deftest is]]
            [hours.overtime :as overtime]
            [cljs-time.core :as time]))

(deftest format-zero-balance
  (is (= (hours.overtime/format-balance 0) "0")))

(deftest format-positive-balance
  (is (= (hours.overtime/format-balance 60) "+1h")))

(deftest format-mixed-balance
  (is (= (hours.overtime/format-balance 80) "+1h20")))

(deftest format-negative-balance
  (is (= (hours.overtime/format-balance -60) "-1h")))

(deftest format-negative-mixed-balance
  (is (= (hours.overtime/format-balance -80) "-1h20")))

(deftest format-minutes-balance
  (is (= (hours.overtime/format-balance 10) "+10m")))

(deftest format-negative-minutes-balance
  (is (= (hours.overtime/format-balance -10) "-10m")))

(deftest parse-hour-overtime
  (is (= (hours.overtime/parse-overtime "1") 60)))

(deftest parse-negative-hour-overtime
  (is (= (hours.overtime/parse-overtime "-1") -60)))

(deftest parse-minutes-overtime
  (is (= (hours.overtime/parse-overtime "10") 10)))

(deftest parse-hour-minutes-overtime
  (is (= (hours.overtime/parse-overtime "120") 80)))

(deftest parse-negative-hour-minutes-overtime
  (is (= (hours.overtime/parse-overtime "-120") -80)))

(deftest get-week-balance-adds-overtimes
  (is (= (overtime/get-week-balance (fn [_] "01") (time/local-date 2020 3 2)) 5)))
