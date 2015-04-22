(ns crochet.components.history
  (:require [reagent.core :as reagent]
            [cljs.core.async :refer [put!]]
            [crochet.app-state :refer [history]]
            [crochet.dispatcher :refer [inbound-chan]]
            [crochet.components.color-preview :refer [color-preview]]))

(defn- restore [revision]
  (put! inbound-chan {:type :restore
                      :data revision}))

(defn- history-item [revision]
  [:li.version {:on-click #(restore revision)}
   [:section.meta
    [:span.size (str (:width revision) "x" (:height revision))]
    [:ul.colors
     (for [color (:colors revision)]
       ^{:key color} [:li [color-preview color]])]]])

(defn history-container [history]
  (let [revisions (:revisions history)]
    [:section.history
     [:header
      [:span.count (str (count revisions) " total")]
      [:h2 "History"]]
     (if (> (count revisions) 0)
       [:ul
         (for [revision revisions]
           [history-item revision])]
       [:div.notice
        [:p "None yet!"]])]))
