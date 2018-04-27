(ns project.core)
(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])
(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn test1
  ([]     (test1 "Hello world!"))
  ([msg]  (println msg)))

(defn -main
  []
  (test1)
  (println (io/resource "airline_delay_causes.csv"))
  (?- (stdout)sentence)
  (println (slurp (io/resource "airline_delay_causes.csv")))
  )