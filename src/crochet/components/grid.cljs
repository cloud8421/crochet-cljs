(ns crochet.components.grid
  (:require [reagent.core :as reagent]
            [cljs.core.async :refer [put!]]
            [crochet.app-state :refer [state generate-random-color]]
            [crochet.dispatcher :refer [inbound-chan]]))

(defn- add-index [colors]
  (map vector (range 1 (+ 1 (count colors))) colors))

(defn to-layer [colors]
  (let [with-index (add-index colors)]
    (map (fn [[index color]]
           (let [z (- (count colors) index)]
             {:color color
              :index index
              :z z
              :offset (float (/ z 2))}))
         with-index)))

(defn- layer [layer-map]
  (let [style {:background-color (:color layer-map)
               :z-index (:z layer-map)
               :width (str (:index layer-map) "em")
               :height (str (:index layer-map) "em")
               :left (str (:offset layer-map) "em")
               :top (str (:offset layer-map) "em")}]
    [:div.layer {:style style}]))

(defn- square [comb]
  (let [comb-length (count comb)
        with-index (add-index comb)
        style {:width (str comb-length "em")
               :height (str comb-length "em")}]
    [:div.square {:style style}
     (for [layer-map (to-layer comb)]
       [layer layer-map])]))

(defn- grid [layout]
  (let [total-width (* (:width layout) (:number-of-layers layout))
        style {:width (str total-width "em")}]
    [:section.grid-container
     [:div.grid {:style style}
      (for [comb (:squares layout)]
        [square comb])]]))

(defn grid-container [layout]
  (if (> (:number-of-layers layout) (count (:colors layout)))
    [:div.notice
     [:p "You need to add more colors"]]
    [grid layout]))