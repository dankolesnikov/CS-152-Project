(ns project.core
  (:require [clojure.java.io :as io] ))
(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn test1
  ([]     (test1 "Hello world!"))
  ([msg]  (println msg)))

(defn -main
  []
  (test1)
  (println (io/resource "airline_delay_causes.csv" )))