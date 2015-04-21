(ns crochet.app-state
  (:require [cljs.core.async :refer [chan pub sub <!]]
            [reagent.core :refer [atom]]
            [crochet.dispatcher :refer [main-publication]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn generate-random-color []
  (str "#" (.toString (rand-int 16rFFFFFF) 16)))

(def layout-defaults {:squares [] :width 4 :height 4
                      :number-of-layers 4 :colors [(generate-random-color)]})
(defrecord Layout [squares width height
                   number-of-layers colors])

(def project-defaults {:name "My new project" :layouts []})
(defrecord Project [name layouts])

(def state (atom {:user {}
                 :projects []
                 :project (map->Project project-defaults)
                 :layout (map->Layout layout-defaults)}))

(defn- randomize-colors [colors amount]
  (repeatedly amount #(shuffle colors)))

(defn- create-random-combination [layout]
  (let [comb-length (* (:width layout) (:height layout))]
    (randomize-colors (:colors layout) comb-length)))

(defn- has-enough-colors [layout]
  (>= (count (:colors layout)) (:number-of-layers layout)))

(defn- generate-square-combination []
  (let [layout (:layout @state)]
    (when (has-enough-colors layout)
      (swap! state update-in [:layout :squares] #(create-random-combination layout)))))

(def projects-chan (chan))
(sub main-publication :projects projects-chan)

(def project-chan (chan))
(sub main-publication :project project-chan)

(def layout-chan (chan))
(sub main-publication :layout layout-chan)

(def colors-chan (chan))
(sub main-publication :colors colors-chan)

(def generate-chan (chan))
(sub main-publication :generate-squares-combination generate-chan)

(go-loop []
         (swap! state assoc :projects (:data (<! projects-chan)))
         (recur))

(go-loop []
         (swap! state assoc :project (:data (<! project-chan)))
         (recur))

(go-loop []
         (swap! state assoc :layout (:data (<! layout-chan)))
         (generate-square-combination)
         (recur))

(go-loop []
         (let [color (:data (<! colors-chan))]
           (swap! state update-in [:layout :colors] #(conj % color))
           (generate-square-combination)
           (recur)))

(go-loop []
         ((fn [_] (generate-square-combination)) (<! generate-chan))
         (recur))
