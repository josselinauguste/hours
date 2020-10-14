(ns hours.db
  (:require
    [re-frame.core :as reframe]
    [cognitect.transit :as transit]
    [cljs-time.core :as time]))

(extend-type goog.date.Date
  IEquiv
  (-equiv [o other]
    (and (instance? goog.date.Date other)
         (== (.valueOf o) (.valueOf other)))))

(deftype DateHandler []
  Object
  (tag [this v] "date")
  (rep [this v] #js [(time/year v) (time/month v) (time/day v)]))

(defn date-reader
  [[y m d]]
  (time/local-date y m d))

(def default-state
  {:overtimes {}})

(reframe/reg-cofx
   :local-store
   (fn [coeffects local-store-key]
      (let [reader (transit/reader :json {:handlers {"date" date-reader}})]
        (assoc coeffects
                :local-store
                (some->> (.getItem js/localStorage local-store-key) (transit/read reader))))))

(defn persist-in-local-store
 [db]
 (let [writer (transit/writer :json {:handlers {goog.date.Date (DateHandler.)}})]
   (.setItem js/localStorage "state" (transit/write writer db))))

(defn get-day [db date]
  (get-in db [:overtimes date] "0"))
