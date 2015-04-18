(ns crochet.components.projects
  (:require [reagent.core :as reagent]
            [crochet.app-state :refer [state]]))

(defn add-new-project-component []
  [:h2 "Add new project here"])

(defn project-component [project]
  [:li (:name project)])

(defn projects-component []
  (let [projects (:projects @state)]
    [:section.projects
     [:ul
      (for [project projects]
        ^{:key (:name project)} [project-component project])]]))
