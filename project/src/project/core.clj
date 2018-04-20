(ns project.core)
(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn test1
  ([]     (test1 "Hello world!"))
  ([msg]  (println msg)))

(defn -main
  "This should be pretty simple."
  []
  (println "Hello, World!"))