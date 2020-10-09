(ns hours.core
  (:require
    [clojure.string]
    [reagent.dom :as rdom]
    [re-frame.core :as rf]
    [cognitect.transit :as t]
    [cljs-time.core :as time]
    [cljs-time.format :as time-format]
    [hours.calendar :as cal]))

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

(rf/reg-cofx
   :local-store
   (fn [coeffects local-store-key]
      (let [reader (t/reader :json {:handlers {"date" date-reader}})]
        (assoc coeffects
                :local-store
                (some->> (.getItem js/localStorage local-store-key) (t/read reader))))))

(defn persist-in-local-store
 [db]
 (let [writer (t/writer :json {:handlers {goog.date.Date (DateHandler.)}})]
   (.setItem js/localStorage "state" (t/write writer db))))

(defn dispatch-overtime-change-event
  [date value]
  (rf/dispatch [:overtime-change date value]))

(rf/reg-event-fx
 :initialize
 [(rf/inject-cofx :local-store "state")]
 (fn [cofx _]
   {:db (or (:local-store cofx) default-state)}))

(rf/reg-event-fx
 :overtime-change
 [(rf/after persist-in-local-store)]
 (fn [cofx [_ date value]]
   {:db (assoc-in (:db cofx) [:overtimes date] value)}))

(rf/reg-sub
 :get-overtime
 (fn [db [_ date]]
   (get-in db [:overtimes date] "0")))

(rf/reg-sub
 :get-week-balance
 (fn [db [_ week]]
   (let [week-days (cal/days-of-week week)]
    (apply + (map #(js/parseFloat (get-in db [:overtimes %] "0")) week-days)))))

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
  (let [overtime @(rf/subscribe [:get-overtime date])]
    [:p.inline-block.text-sm.text-center
    (day-name date)
    [:br]
    [:input.w-6.text-center {:on-change #(dispatch-overtime-change-event date (.. % -target -value))
                             :type "text"
                             :value overtime
                             :maxLength 3}]]))

(defn format-balance [balance]
  (if (>= balance 0)
    (clojure.string/join "" ["+" balance "h"])
    (clojure.string/join "" ["-" (- 0 balance) "h"])))

(defn week [week]
  (let [week-balance @(rf/subscribe [:get-week-balance week])]
    [:div.mt-2
     [:div
      [:span.inline-block.align-middle (time-format/unparse (time-format/formatter "d/MM") (first (cal/days-of-week week)))]
      [:span.text-xs.text-gray-600.inline-block.m-3 (format-balance week-balance)]]
     [:div.grid.grid-cols-5
      (for [d (cal/days-of-week week)]
        ^{:key d} [day d])]]))

(defn month [for-day]
  [:div.mt-4
   (for [w (reverse (cal/weeks for-day))]
     ^{:key w} [week w])])

(defn hours-app []
  [:div.container.p-4.mx-auto.md:max-w-lg.text-gray-900
    [:h1.mt-4.text-2xl "Hours"]
    (month (time/today))
    ])

(defn mount-ui
  []
  (rdom/render [hours-app] (js/document.getElementById "app")))

(defn main! []
  (rf/dispatch-sync [:initialize])
  (mount-ui))

(defn reload! []
  (mount-ui)
  (prn "Reloaded"))
