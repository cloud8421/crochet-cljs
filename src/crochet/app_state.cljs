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

(def history (atom {:revisions []}))

(def state (atom {:user {}
                 :projects []
                 :project (map->Project project-defaults)
                 :layout (map->Layout layout-defaults)}))

(defn- generate-square-combination! []
  (let [layout (:layout @state)]
    (when (has-enough-colors layout)
      (swap! state update-in [:layout :squares] #(create-random-combination layout))
      (swap! history update-in [:revisions] #(conj % (:layout @state))))))

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
         (generate-square-combination!)
         (recur))

(go-loop []
         (let [color (:data (<! colors-chan))]
           (swap! state update-in [:layout :colors] #(conj % color))
           (generate-square-combination!)
           (recur)))

(go-loop []
         ((fn [_]
            (generate-square-combination!)) (<! generate-chan))
         (recur))
