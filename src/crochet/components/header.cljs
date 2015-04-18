(ns crochet.components.header
  (:require [reagent.core :as reagent]))

(defn header-component []
  [:header {:role "banner"}
   [:h1 "Crochet"]
   [:nav.main-nav
    [:ul
     [:li
      [:a {:href "#/"} "All projects"]]
     [:li
      [:a {:href "#/new-project"} "Add new"]]]]])
