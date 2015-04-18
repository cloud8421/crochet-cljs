(ns crochet.core
  (:require [reagent.core :as reagent]
            [secretary.core :as sec :refer-macros [defroute]]
            [crochet.app-state :refer [state]]
            [crochet.transport :refer [fetch-projects]]
            [crochet.routing :refer [init-routing!]]
            [crochet.components.header :refer [header-component]]
            [crochet.components.projects :refer [projects-component add-new-project-component]]))

(enable-console-print!)

(defn get-main-container []
  (. js/document (getElementById "main")))

(defn get-nav-container []
  (. js/document (getElementById "navigation")))

(defroute "/" []
  (reagent/render-component [projects-component]
                            (get-main-container)))

(defroute "/new-project" []
  (reagent/render-component [add-new-project-component]
                            (get-main-container)))

(init-routing!)

(reagent/render-component [header-component]
                          (get-nav-container))

(fetch-projects 12345)
