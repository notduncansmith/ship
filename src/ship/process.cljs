(ns ship.process
  (:require
    [cljs.core.match :refer-macros [match]]
    [cljs.tools.reader :refer [read-string]]
    [cljs.js :refer [empty-state eval js-eval]]))

(def hello
  "A process whose model has a single property of constant value"
  { :hello "world" :model [{:hello (fn [state msg] "world")}]})

(def echo
  "A process whose model has a single property whose value is the last message received."
  { :echo nil :model [{:echo (fn [state msg] msg)}]})

(def log
  "A process which keeps a vector of all messages received. Prints messages as received."
  { :log [] :model [{:log #(conj (% :log) (do (println %2) %2))}]})

(def transformer
  "A process which applies any given message's `:transform` property to its internal state."
  { :state true
    :model [{:state #(if (%2 :transform) (update % :state (%2 :transform)) (% :state))}]})

(defn eval-str [s]
  (eval (empty-state)
        (read-string s)
        {:eval js-eval :source-map true :context :expr}
        (fn [result] result)))

(def evaluator
  "A process which evaluates any string of Javascript sent to it."
  { :result nil
    :model [{:result #(eval-str %2)}]})

(defn forward-or-back
  [history destination]
  (let [prev (if (> (count history) 1) (get history (- (count history) 2)) nil)
        k= #(= (prev %) (destination %))
        to-prev (or (= destination -1) (and (not= nil prev) (k= :pid) (k= :path)))]
    (if to-prev
      (vec (drop-last history))
      (conj history destination))))

(def tab-welcome {:history [{:pid :welcome :path []}]})
(def tabs
  { :tabs {:welcome tab-welcome}
    :active-tab-id :welcome
    :active-tab tab-welcome
    :model
      [{:tabs
        (fn [state msg]
          (match msg
            {:navigate [tab-id to]} (update-in (state :tabs) [tab-id :history] #(forward-or-back % to))
            {:close tab-id} (dissoc tab-id (state :tabs))))
        :active-tab-id #(or (%2 :activate) (% :active-tab-id))}
       {:active-tab (fn [state msg] (get (state :tabs) (state :active-tab-id)))}]})

