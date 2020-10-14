(ns hours.events
  (:require
    [re-frame.core :as reframe]
    [hours.db]))

(defn dispatch-overtime-change-event
  [date value]
  (reframe/dispatch [:overtime-change date value]))

(reframe/reg-event-fx
 :initialize
 [(reframe/inject-cofx :local-store "state")]
 (fn [cofx _]
   {:db (or (:local-store cofx) hours.db/default-state)}))

(reframe/reg-event-fx
 :overtime-change
 [(reframe/after hours.db/persist-in-local-store)]
 (fn [cofx [_ date value]]
   {:db (assoc-in (:db cofx) [:overtimes date] value)}))
