(require '[cljs.closure :as cljsc])

(cljsc/watch "src"
  {:main 'crochet.core
   :output-to "out/crochet.js"
   :output-dir "out"
   :optimizations :none
   :cache-analysis true
   :source-map true})
