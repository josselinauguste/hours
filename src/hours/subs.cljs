(ns hours.subs
  (:require
    [re-frame.core :as reframe]
    [hours.db]
    [hours.overtime]))

(reframe/reg-sub
 :get-overtime
 (fn [db [_ date]]
   (hours.db/get-day db date)))

(reframe/reg-sub
 :get-week-balance
 (fn [db [_ week]]
    (hours.overtime/get-week-balance #(hours.db/get-day db %) week)))
