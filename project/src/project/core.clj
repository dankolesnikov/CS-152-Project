(ns project.core)
(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[clojure.string :as str])
(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn test1
  ([]     (test1 "Hello world!"))
  ([msg]  (println msg)))

(defn names [x] (list (nth x 3) (nth x 6) (nth x 7)))

(defn genNames [x] (if (< (count x) 21) []
                     (do 
                       (def entry (names x))
                       (def remaining (drop 10 x))
                       entry
                     )  
                     ))

;; High school math
(defn percentage-delay [num total]
  (* (/ total num) 100))

;; Clojure only provides first and second
(def fourth #(nth % 3))

(def fifth #(nth % 4))

(def sixth #(nth % 5))

(def seventh #(nth % 6))

(def eighth #(nth % 7))


;; Extract the interesting part
(def extract (juxt fourth seventh eighth))

;; Parse the data
(defn parse [airname-data-row]
  (let [[date open close] (extract airname-data-row)]
    [date (percentage-delay (Double/parseDouble open)
                             (Double/parseDouble close))]))

(defn -main
  []
  (test1)
  (println (io/resource "airline_delay_causes.csv"))
  ;(?- (stdout)sentence)
  (def x (str/split(slurp (io/resource "airline_delay_causes.csv")) #","))
  ;x now contains each element of the CSV seperated into a list
  (def y (parse ["2018" "1" "\"9E\"" "\"Endeavor Air Inc.\"" "\"ABY\"" "\"Albany  GA: Southwest Georgia Regional\"" "83.00" "10.00" "3.60" "0.98" "1.51" "0.00" "3.91" "3.00" "0.00" "685.00" "106.00" "240.00" "43.00" "0.00" "296.00"]))
  (println y)
  )