(ns crochet.layout)

(defn- index-of [coll v]
  (let [i (count (take-while #(not= v %) coll))]
    (when (or (< i (count coll))
            (= v (last coll)))
      i)))

(defn generate-random-color []
  (str "#" (.toString (rand-int 16rFFFFFF) 16)))

(def layout-defaults {:squares [] :width 4 :height 4
                      :number-of-layers 4 :colors [(generate-random-color)]})
(defrecord Layout [squares width height
                   number-of-layers colors])

(defn- randomize-colors [colors amount pick-count]
  (repeatedly amount #(into [] (take pick-count (shuffle colors)))))

(defn- generate-random-combination [layout]
  (let [comb-length (* (:width layout) (:height layout))]
    (randomize-colors (:colors layout) comb-length (:number-of-layers layout))))

(defn- has-enough-colors [layout]
  (>= (count (:colors layout)) (:number-of-layers layout)))

(defn- replace-color-in-vec [colors old-color new-color]
  (assoc colors (index-of colors old-color) new-color))

(defn add-color [layout color]
  (update-in layout [:colors] #(conj % color)))

(defn replace-color [layout old-color new-color]
  (-> layout
      (update-in [:colors] #(replace-color-in-vec % old-color new-color))
      (update-in [:squares] #(map (fn [sq] (replace-color-in-vec sq old-color new-color)) %))))

(def sample-layout (map->Layout layout-defaults))

(defn- create-random-combination [layout]
  (assoc layout :squares (generate-random-combination layout)))
