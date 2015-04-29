(ns crochet.app-state
  (:require [cljs.core.async :refer [chan pub sub <!]]
            [reagent.core :refer [atom]]
            [crochet.layout :refer [Layout layout-defaults map->Layout
                                    generate-random-color has-enough-colors
                                    create-random-combination]]
            [crochet.dispatcher :refer [main-publication]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(def project-defaults {:name "My new project" :layouts []})
(defrecord Project [name layouts])

(def max-history 50)
(def history (atom {:revisions '()}))

(def state (atom {:user {}
                 :projects []
                 :project (map->Project project-defaults)
                 :layout (map->Layout layout-defaults)}))

(defn- generate-square-combination! []
  (let [layout (:layout @state)]
    (when (has-enough-colors layout)
      (swap! state update-in [:layout :squares] #(create-random-combination layout))
      (swap! history update-in [:revisions] (fn [current]
                                              (->> (conj current (:layout @state))
                                                   (take max-history)))))))

(defn- update-color [collection old-color new-color]
  (let [old-color-index (index-of collection old-color)]
    (update-in collection [old-color-index] (fn [_] new-color))))

(defn- replace-color-in-squares [squares old-color new-color]
  (map (fn [current] (update-color current old-color new-color))
       squares))

(def projects-chan (chan))
(sub main-publication :projects projects-chan)

(def project-chan (chan))
(sub main-publication :project project-chan)

(def layout-chan (chan))
(sub main-publication :layout layout-chan)

(def add-color-chan (chan))
(sub main-publication :add-color add-color-chan)

(def update-color-chan (chan))
(sub main-publication :update-color update-color-chan)

(def generate-chan (chan))
(sub main-publication :generate-squares-combination generate-chan)

(def restore-chan (chan))
(sub main-publication :restore restore-chan)

(go-loop []
         (swap! state assoc :projects (:data (<! projects-chan)))
         (recur))

(go-loop []
         (swap! state assoc :project (:data (<! project-chan)))
         (recur))

(go-loop []
         (swap! state assoc :layout (:data (<! layout-chan)))
         (generate-square-combination!)
         (recur))

(go-loop []
         (let [color (:data (<! add-color-chan))]
           (swap! state update-in [:layout :colors] #(conj % color))
           (generate-square-combination!)
           (recur)))

(defn- index-of [coll v]
  (let [i (count (take-while #(not= v %) coll))]
    (when (or (< i (count coll))
            (= v (last coll)))
      i)))

(go-loop []
         (let [colors (:data (<! update-color-chan))]
           (swap! state update-in [:layout :colors] (fn [current] (update-color current (:old colors) (:new colors))))
           (swap! state update-in [:layout :squares] (fn [current] (replace-color-in-squares current (:old colors) (:new colors))))
           (recur)))

(go-loop []
         ((fn [_]
            (generate-square-combination!)) (<! generate-chan))
         (recur))

(go-loop []
         (swap! state assoc :layout (:data (<! restore-chan)))
         (recur))
