(ns project.core)
(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[clojure.string :as str])
(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn- parse-str [^String s]
  (seq (.split s ",")))

(defn- parse-strings [^String name]
  (hfs-textline name))

(def say-hello
 (fn [name]
   (str "Hello " name)))

;;not working. Intention is to parse line by line carrier and flight data
(def airline-data
  (let [source (hfs-textline (.getPath(io/resource "airline_delay_causes_2012_2017.csv")))]
   (<- [?airline ?flights]
       (source ?line)
       ;;we are only looking for the 4th column(carrier name) and 7th column (total flights)
       (parse-str ?line :> _ _ _ ?airline _ _ ?flights _ _ _ _ _ _ _ _ _ _ _ _ _ _)
       (:distinct false))
   ))

(def basic-ass-data
  (let [source (hfs-textline (.getPath(io/resource "Test.txt")))]
   (<- [?line]
       (source ?line))
   ))

(def print-test 
  (let [text-tap (hfs-textline (.getPath(io/resource "airline_delay_causes_2012_2017.csv")))]
  (?<- (stdout) [?textline]
      (text-tap ?textline)))
)   


(defn -main
  []
  (def items (basic-ass-data 2))
  (println items)
  ;(let [text-tap (hfs-textline (.getPath(io/resource "Test.txt")))]
  ;   (?<- (println) [?textline]
  ;       (text-tap ?textline)))
  (println (basic-ass-data 2))
  (println (say-hello "Jason"))
  (println (airline-data 10))
  ;test gets path of our csv file resource
  ;(println print-test)
  (println (.getPath(io/resource "airline_delay_causes_2012_2017.csv")))
  
  
  ;test parse-str on single example line of our csv
  (println (parse-str "\"year\",\" month\",\"carrier\",\"carrier_name\",\"airport\",\"airport_name\",\"arr_flights\",\"arr_del15\",\"carrier_ct\",\" weather_ct\",\"nas_ct\",\"security_ct\",\"late_aircraft_ct\",\"arr_cancelled\",\"arr_diverted\",\" arr_delay\",\" carrier_delay\",\"weather_delay\",\"nas_delay\",\"security_delay\",\"late_aircraft_delay\","))
  )
