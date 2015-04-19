(ns crochet.components.projects
  (:require [reagent.core :as reagent]
            [crochet.app-state :refer [state]]))

(defn- name-control []
  [:div.name-control
   [:label {:for "name"} "Name"]
   [:input {:type "text" :id "name" :value "My new project"}]])

(defn- layout-controls []
  [:div.layout-controls
   [:div.width-control
    [:label {:for "width"} "Width"]
    [:input {:type "number" :id "width" :value 4}]]
   [:div.height-control
    [:label {:for "height"} "Height"]
    [:input {:type "number" :id "height" :value 4}]]
   [:div.number-of-layers-control
    [:label {:for "number-of-layers"} "Layers"]
    [:input {:type "number" :id "number-of-layers" :value 3}]]
   [:button {:id "generate"} "Generate"]])

(defn add-new-project-component []
  [:section.new-project
   [:section.workspace
    [:section.editor
     [name-control]
     [:h2 "Dimensions"]
     [layout-controls]]]])

(defn project-component [project]
  [:li (:name project)])

(defn projects-component []
  (let [projects (:projects @state)]
    [:section.projects
     [:ul
      (for [project projects]
        ^{:key (:name project)} [project-component project])]]))
