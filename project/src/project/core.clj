(ns project.core)
(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[clojure.data.csv :as csv])

(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn- parse-str [^String s]
  (seq (.split s ",")))

(def flight-data (hfs-textline "resources/airline_delay_causes_2018.csv"))

(defn flight-parser 
  "function that parses city,state string into city and state"
  [line]
  (map #(.trim %) (first (csv/read-csv line))))
  
(defn city-state-query 
  "query that outputs all city and state pairs from cities dataset; repl usage: (city-state-query)"
  []
  (?<- (stdout) [?city ?state]
    (flight-data ?line)
    (flight-parser ?line :> ?city ?state)))

(defn- parse-strings [^String name]
  (hfs-textline name))

(def say-hello
 (fn [name]
   (str "Hello " name)))

;;not working. Intention is to parse line by line carrier and flight data
; (def airline-data
;   (let [source (hfs-textline (.getPath(io/resource "airline_delay_causes_2012_2017.csv")))]
;    (<- [?airline ?flights]
;        (source ?line)
;        ;;we are only looking for the 4th column(carrier name) and 7th column (total flights)
;        (parse-str ?line :> _ _ _ ?airline _ _ ?flights _ _ _ _ _ _ _ _ _ _ _ _ _ _)
;        (:distinct false))
;    ))
;; Path to simple text file "/Users/danil/Desktop/textfile.txt"
; (def print-test 
;   (let [hfs-delimite (text-line "/Users/danil/Desktop/textfile.txt")]
;   (?<- (stdout) [?textline]
;       (text-tap ?textline)))
; )   


(defn -main
  []
  (println (say-hello "Jason"))
  ; (println (airline-data 10))
  ;test gets path of our csv file resource
  ;(println print-test)
  ;(println (.getPath(io/resource "airline_delay_causes_2012_2017.csv")))
  (println city-state-query)
  
  ;test parse-str on single example line of our csv
  ;(println (parse-str "\"year\",\" month\",\"carrier\",\"carrier_name\",\"airport\",\"airport_name\",\"arr_flights\",\"arr_del15\",\"carrier_ct\",\" weather_ct\",\"nas_ct\",\"security_ct\",\"late_aircraft_ct\",\"arr_cancelled\",\"arr_diverted\",\" arr_delay\",\" carrier_delay\",\"weather_delay\",\"nas_delay\",\"security_delay\",\"late_aircraft_delay\","))
  )
