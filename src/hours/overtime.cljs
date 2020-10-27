(ns hours.overtime
  (:require
   [clojure.string]
   [hours.calendar]))

(defn format-balance [balance]
  (defn abs "(abs n) is the absolute value of n" [n]
    (if (neg? n)
      (- n)
      n))
  (defn hour [hours minutes]
    (if (= 0 hours)
      (if (= 0 minutes)
        "0"
        (clojure.string/join "" [minutes "m"]))
      (if (= 0 minutes)
        (clojure.string/join "" [hours "h"])
        (clojure.string/join "" [hours "h" minutes]))))
  (defn sign [s]
    (cond
      (> balance 0) (clojure.string/join "" ["+" s])
      (< balance 0) (clojure.string/join "" ["-" s])
      :else s))
  (let [abs-balance (abs balance)
        hours (int (/ abs-balance 60))
        minutes (- abs-balance (* hours 60))]
    (sign (hour hours minutes))))

(defn parse-overtime [overtime]
  (let [r (re-matches #"^([+-]?)(\d{1,3})" overtime)
        full (first r)
        sign (second r)
        digits (last r)]
    (cond
      (not r) 0
      (= (count digits) 3) (let [absolute (+ (* (js/parseInt (subs digits 0 1)) 60) (js/parseInt (subs digits 1)))]
                             (if (= "-" sign)
                               (- absolute)
                               absolute))
      (= (count digits) 2) (js/parseInt full)
      (= (count digits) 1) (* (js/parseInt full) 60)
      )))

(defn get-week-balance [get-day week]
  (let [week-days (hours.calendar/days-of-week week)]
    (apply + (map #(parse-overtime (get-day %)) week-days))))
