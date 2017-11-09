(ns ship.ui
  (:require
    [cljs.core.match :refer-macros [match]]
    [clojure.string :as s]))

(defn change-handler [f] (fn [e] (f (-> e .-target .-value))))
(defn excise
  "Return a vector with all the elements in v (in the same order) except for the nth"
  [v n]
  (into (subvec v 0) (subvec v (inc n))))

(defn text-editor
  [label value on-change]
  [:div.box {}
    [:span.label {} label]
    [:textarea {:value value :on-change (change-handler on-change)}]])

(defn number-row
  [label value send!]
  [:div.box {}
    [:span.label {} label]
    [:input {:type "number" :value value :on-change (change-handler send!)}]])

(defn boolean-row
  [label value send!]
  [:div.box {}
    [:span.label {} label]
    [:input {:type "checkbox" :value value :on-change (change-handler send!)}]])

(defn cljs-row
  [label value send!]
  [:div.box {}
    [:span.label {} (str (or label "λ"))]
    ; todo - parser/validity feedback
    [:textarea {:value value :on-change (change-handler send!)}]])

(defn editor
  [location value transform]
  (let [on-change (fn [v] (transform #(update-in % (location :path) v)))]
    (match (type value)
      String [:textarea {:value value :on-change (change-handler on-change)}]
      Number [:input {:type "text" :value value :on-change (change-handler on-change)}]
      Boolean [:input {:type "checkbox" :value value :on-change (change-handler on-change)}]
      Function [:textarea {:value value :on-change (change-handler on-change)}]
      cljs.core/Keyword [:input {:type "text" :value value :on-change (change-handler (comp on-change keyword))}]
      :else [:textarea {:value value :on-change (change-handler on-change)}])))

(defn map-editor
  [location value transform]
  (let [p (location :path)
        item (fn [k v] [editor {:location (update location :path conj k)
                                :value v :transform transform}])
        items (into [:ol.vector-items {}] (map (fn [k v] [:li {} [item k v]]) value))]
    [:div.box {}
      [:span.label {} (last p)]
      items
      [:p.add
        {:on-click #(transform (fn [v] (update-in v (location :path) conj {})))}
        "+ Add element"]]))

(defn vector-editor
  [location value on-change]
  (let [p (location :path)
        item (fn [k v] [editor {:location (update location :path conj k)
                                :value v :transform transform}])
        items (into [:ol.items {}] (map-indexed (fn [k v] [:li {} [item k v]]) value))]
    [:div.box {}
      [:span.label {} (last path)]
      items
      [:p.add
        {:on-click #(transform (fn [v] (update-in v (location :path) conj {})))}
        "+ Add element"]]))

(defn box
  [{:keys [location value send-self! navigate!]}]
  (let [t (type value)
        is-map (= t cljs.core/PersistentArrayMap)
        is-vec (= t cljs.core/PersistentArray)
        {:keys [pid path]} location
        label (last path)
        editable (= (first path) :model)
        back-out #(navigate! {:pid pid :path (drop-last path)})]
    [:div.box {}
      [:span.label {} (if (= path []) ">" (str path))]
      (or (value :view)
        (if editable
          (cond
            is-map [map-editor location value #(send-self! {:update-model %})]
            is-vec [vector-editor location value #(send-self! {:update-model %})])
          [display location value]))]))

(defn navbar
  [{:keys [history on-navigate]}]
  [:div.navbar {}
    [:span {:on-click #(on-navigate -1)} "←"]
    (into [:select {:value (dec (count history))
                    :on-change (change-handler #(on-navigate (nth history (js/parseInt % 10))))}]
          (map-indexed #(conj [:option {:value %}] (str (%2 :pid) ">" (%2 :path)))
                       history))])

(defn tab-selector
  [processes]
  (let [{:keys [send! tabs]} @processes
        history (get-in tabs [:active-tab :history])
        location (last history)
        tab-value (get-in (processes (location :pid)) (location :path))
        navigate! #(send! :tabs {:navigate [(tabs :active-tab-id) %]})]
    [:div.active-tab {}
      [navbar {:history history :on-navigate navigate!}]
      [box { :value tab-value
             :location location
             :send! send!
             :navigate! navigate!
             :send-self! (partial send! ((last history) :pid))}]]))

(defn app
  [app-state]
  [tab-selector (r/cursor app-state [:processes])])
