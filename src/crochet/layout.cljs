(ns crochet.layout)

(defn generate-random-color []
  (str "#" (.toString (rand-int 16rFFFFFF) 16)))

(def layout-defaults {:squares [] :width 4 :height 4
                      :number-of-layers 4 :colors [(generate-random-color)]})
(defrecord Layout [squares width height
                   number-of-layers colors])

(defn- randomize-colors [colors amount pick-count]
  (vec (repeatedly amount #(take pick-count (shuffle colors)))))

(defn- create-random-combination [layout]
  (let [comb-length (* (:width layout) (:height layout))]
    (randomize-colors (:colors layout) comb-length (:number-of-layers layout))))

(defn- has-enough-colors [layout]
  (>= (count (:colors layout)) (:number-of-layers layout)))
