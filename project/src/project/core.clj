(ns project.core)
(use 'cascalog.api)
(use 'cascalog.playground)

(defn test1
  ([]     (test1 "Hello world!"))
  ([msg]  (println msg)))
