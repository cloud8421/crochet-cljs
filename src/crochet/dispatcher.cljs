(ns crochet.dispatcher
  (:require [cljs.core.async :refer [chan pub sub <!]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(def inbound-chan (chan))
(def debug-chan (chan))

(def main-publication
  (pub inbound-chan #(:type %)))

;; (sub main-publication :projects debug-chan)
;;
;; (defn take-and-print [channel prefix]
;;   (go-loop []
;;     (.log js/console (clj->js (<! channel)))
;;     (recur)))
;;
;; (take-and-print debug-chan)
