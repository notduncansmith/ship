(ns ship.core
  (:require [factfold.model :refer [apply-model]]))

(defn receive
  "Advance a process in the context of information"
  ; note - rather permissive
  [state msg]
  (if state (apply-model (state :model) state [msg]) msg))
