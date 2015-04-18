(ns crochet.routing
  (:require [secretary.core :as sec]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(defn- hook-browser-navigation! []
  (doto (History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (sec/dispatch! (.-token event))))
    (.setEnabled true)))

(defn init-routing! []
  (sec/set-config! :prefix "#")
  (hook-browser-navigation!))
