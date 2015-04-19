(ns crochet.app-state
  (:require [cljs.core.async :refer [chan pub sub <!]]
            [reagent.core :refer [atom]]
            [crochet.dispatcher :refer [main-publication]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))


(def layout-defaults {:squares [] :width 4 :height 4
                      :number-of-layers 4 :colors []})
(defrecord Layout [squares width height
                   number-of-layers colors])

(def project-defaults {:name "My new project" :layouts []})
(defrecord Project [name layouts])

(def state (atom {:user {}
                 :projects []
                 :project (map->Project project-defaults)
                 :layout (map->Layout layout-defaults)}))

(def projects-chan (chan))
(sub main-publication :projects projects-chan)

(def project-chan (chan))
(sub main-publication :project project-chan)

(def layout-chan (chan))
(sub main-publication :layout layout-chan)

(go-loop []
  (swap! state assoc :projects (:data (<! projects-chan)))
  (recur))

(go-loop []
  (swap! state assoc :project (:data (<! project-chan)))
  (recur))

(go-loop []
  (swap! state assoc :layout (:data (<! layout-chan)))
  (recur))
