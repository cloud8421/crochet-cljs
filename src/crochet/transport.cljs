(ns crochet.transport
  (:require [ajax.core :refer [GET POST PUT]]
            [cljs.core.async :refer [put!]]
            [crochet.dispatcher :refer [inbound-chan]]))

(defonce base-url "http://localhost:3000/")

(defn handle [type data]
  (put! inbound-chan {:type type
                      :data data}))

(defn- error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn fetch-projects [user-id]
  (GET (str base-url user-id "/projects")
       {:keywords? true
        :response-format :json
        :handler #(handle :projects %)
        :error-handler error-handler}))
