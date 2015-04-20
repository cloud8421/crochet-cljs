(ns crochet.components.grid
  (:require [reagent.core :as reagent]
            [cljs.core.async :refer [put!]]
            [crochet.app-state :refer [state generate-random-color]]
            [crochet.dispatcher :refer [inbound-chan]]))

(defn- build-layers [colors]
  (map vector colors (range 1 (+ 1 (count colors)))))

(defn- layer [color index z offset]
  (let [style {:background-color color
               :z-index z
               :width (str index "em")
               :height (str index "em")
               :left (str offset "em")
               :top (str offset "em")}]
    [:div.layer {:style style}]))

(defn- square [colors]
  (let [colors-length (count colors)
        with-index (build-layers colors)
        style {:width (str colors-length "em")
               :height (str colors-length "em")}]
    [:div.square {:style style}]))

(defn- grid [layout]
  (let [total-width (* (:width layout) (:number-of-layers layout))
        style {:width (str total-width "em")}]
    [:section.grid-container
     [:div.grid {:style style}
      ;; (for [combination (:squares layout)]
      ;;   [square combination]
      "some squares"]]))

(defn grid-container [layout]
  (if (> (:number-of-layers layout) (count (:colors layout)))
    [:div.notice
     [:p "You need to add more colours"]]
    [grid layout]))
