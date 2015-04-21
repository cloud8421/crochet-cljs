(ns crochet.components.editor
  (:require [reagent.core :as reagent]
            [cljs.core.async :refer [put!]]
            [crochet.layout :refer [generate-random-color]]
            [crochet.app-state :refer [state]]
            [crochet.dispatcher :refer [inbound-chan]]
            [crochet.components.grid :refer [grid-container]]))

(defn- update-state [entity attribute value]
  (let [current (entity @state)
        updated (assoc current attribute value)]
    (put! inbound-chan {:type entity
                        :data updated})))

(defn- update-layout [attribute value]
  (update-state :layout attribute value))

(defn- update-project [attribute value]
  (update-state :project attribute value))

(defn- add-new-color []
  (let [color (generate-random-color)]
    (put! inbound-chan {:type :colors
                        :data color})
    :ok))

(defn- generate-squares-combination []
  (put! inbound-chan {:type :generate-squares-combination})
  :ok)

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
   [:button {:id "generate" :on-click #(generate-squares-combination)} "Generate"]])

(defn- color-preview [color]
  [:span.color-preview {:style {:background-color color}}])

(defn- color-controls [colors]
  [:section.colors-container
   [:ul.colors
    (for [color colors]
      ^{:key color} [:li [color-preview color]])
    [:li.add-new
     [:button {:on-click add-new-color} "Add a new color"]]]])

(defn editor-component []
  (let [project (:project @state)
        layout (:layout @state)
        colors (:colors layout)]
    [:section.new-project
     [:section.workspace
      [:section.editor
       [name-control project]
       [:h2 "Dimensions"]
       [layout-controls layout]
       [:h2 "Colors"]
       [color-controls colors]]
      [grid-container layout]]]))
