(ns crochet.components.projects
  (:require [reagent.core :as reagent]
            [crochet.app-state :refer [state]]))

(defn- update-layout [key value]
  (let [layout (:layout @state)
        updated-layout (assoc layout key value)]
    (swap! state assoc :layout updated-layout)))

(defn- update-project [key value]
  (let [project (:project @state)
        updated-project (assoc project key value)]
    (swap! state assoc :project updated-project)))

(defn- name-control [project]
  [:div.name-control
   [:label {:for "name"} "Name"]
   [:input {:type "text"
            :id "name"
            :on-change #(update-project :name (-> % .-target .-value))
            :value (:name project) }]])

(defn- layout-controls [layout]
  [:div.layout-controls
   [:div.width-control
    [:label {:for "width"} "Width"]
    [:input {:type "number"
             :id "width"
             :on-change #(update-layout :width (-> % .-target .-value))
             :value (:width layout)}]]
   [:div.height-control
    [:label {:for "height"} "Height"]
    [:input {:type "number"
             :id "height"
             :on-change #(update-layout :height (-> % .-target .-value))
             :value (:height layout)}]]
   [:div.number-of-layers-control
    [:label {:for "number-of-layers"} "Layers"]
    [:input {:type "number"
             :id "number-of-layers"
             :on-change #(update-layout :number-of-layers (-> % .-target .-value))
             :value (:number-of-layers layout)}]]
   [:button {:id "generate"} "Generate"]])

(defn add-new-project-component []
  (let [project (:project @state)
        layout (:layout @state)]
    [:section.new-project
     [:section.workspace
      [:section.editor
       [name-control project]
       [:h2 "Dimensions"]
       [layout-controls layout]]]]))

(defn project-component [project]
  [:li (:name project)])

(defn projects-component []
  (let [projects (:projects @state)]
    [:section.projects
     [:ul
      (for [project projects]
        ^{:key (:name project)} [project-component project])]]))
