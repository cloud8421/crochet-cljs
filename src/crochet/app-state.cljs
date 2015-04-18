(ns crochet.app-state
  (:require [cljs.core.async :refer [chan pub sub <!]]
            [reagent.core :refer [atom]]
            [crochet.dispatcher :refer [main-publication]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(def state (atom {:user {}
                 :projects []}))

(def projects-chan (chan))
(sub main-publication :projects projects-chan)

(go-loop []
  (swap! state assoc :projects (:data (<! projects-chan)))
  (recur))
