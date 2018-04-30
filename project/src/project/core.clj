(ns project.core)
(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[clojure.string :as str])
(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn test1
  ([]     (test1 "Hello world!"))
  ([msg]  (println msg)))

; Returns a list 4th, 7th, 8th element 
(defn names [x] (list (nth x 3) (nth x 6) (nth x 7)))

;; param: list of lists and calls names on each list
(defn genNames [x] (if (< (count x) 21) []
                     (do 
                       (def entry (names x))
                       (def remaining (drop 10 x))
                       entry
                     )  
                     ))

;; Calculate percentage delay
;; param: num - number of flights 
;; param: total - number of delayed flights
;; divide total by num
(defn percentage-delay [num total]
  (* (/ total num) 100))

;; Clojure only provides first and second
;; Return nth element ?
(def fourth #(nth % 3)) ; Carrier
(def seventh #(nth % 6)) ; Total number of flights
(def eighth #(nth % 7)) ; total number of delays

;; Extract the interesting part
;; Return list of forth, seventh and eigth
(def extract (juxt fourth seventh eighth))

;; Parse the data
(defn parse [airname-data-row]
;; let -> pass in a list of 3 values
  (let [[date open close] (extract airname-data-row)]
    [date (percentage-delay (Double/parseDouble open)
                             (Double/parseDouble close))]))

(defn max-change [csv-file]
  (with-open [reader (io/reader csv-file)]
    (->> (rest (csv/read-csv reader))
         (map parse)
         (apply max-key second))))

(defn -main
  []
  (test1)
  (println (io/resource "airline_delay_causes.csv"))
  ;(?- (stdout)sentence)
  (def x (str/split(slurp (io/resource "airline_delay_causes.csv")) #","))
  ;x now contains each element of the CSV seperated into a list
  (def y (parse ["2018" "1" "\"9E\"" "\"Endeavor Air Inc.\"" "\"ABY\"" "\"Albany  GA: Southwest Georgia Regional\"" "83.00" "10.00" "3.60" "0.98" "1.51" "0.00" "3.91" "3.00" "0.00" "685.00" "106.00" "240.00" "43.00" "0.00" "296.00"]))
  (println)
  (println y)
  ; (def z (rest(with-open [reader (io/reader (io/resource "airline_delay_causes.csv"))]
  ; (doall
  ;   (csv/read-csv reader)))))
  ;(println z)
  (println)
  ;(def x1 (max-change (io/resource "airline_delay_causes.csv")))
  )
