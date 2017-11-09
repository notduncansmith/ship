(ns ship.app
  (:require
    [cljs.core.match :refer-macros [match]]
    [cljs.core.async :refer [>! <! chan]]
    [reagent.core :as r]
    [ship.ui :as ui]
    [ship.process :as process]
    [ship.core :refer [receive]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)
(defn on-js-reload [] (println "Reload"))

(defonce messages (chan 1000))
(defn send! [pid msg] (go (>! messages [pid msg])))
(defonce state (r/atom {:processes {:send! send!
                                    :tabs process/tabs
                                    :welcome process/hello}
                        :log []
                        :model [{:processes #(update % :processes (fn [process-state] (update process-state (first %2) receive (second %2))))
                                 :log #(conj (% :log) %2)}]}))

(go-loop [[pid msg] [:welcome {:hello "World"}]]
  (swap! state receive (if (= pid 0) msg [pid msg]))
  (recur (<! messages)))

(r/render [ui/app state] (js/document.getElementById "app"))
