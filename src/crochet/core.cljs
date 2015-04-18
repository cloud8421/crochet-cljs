(ns crochet.core
  (:require [reagent.core :as reagent]
            [secretary.core :as sec :refer-macros [defroute]]
            [crochet.app-state :refer [state]]
            [crochet.transport :refer [fetch-projects]]
            [crochet.routing :refer [init-routing!]]
            [crochet.components.header :refer [header-component]]))

(enable-console-print!)

(defn get-container []
  (. js/document (getElementById "app")))

(defroute "/" []
  (.log js/console (clj->js @state))
  (println "all projects"))

(defroute "/new-project" []
  (println "new project"))

(init-routing!)

(reagent/render-component [header-component]
                          (get-container))
