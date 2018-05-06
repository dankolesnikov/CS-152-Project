(ns project.core)
(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[clojure.data.csv :as csv])

(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn- parse-str [^String s]
  (seq (.split s ",")))

(defmapfn ->values [val] val)

; csv data file
(def flight-data (hfs-textline "resources/airline_delay_causes_2018.csv"))

(defn flight-parser 
  "parses csv file"
  [line]
  (map #(.trim %) (first (csv/read-csv line))))

(defn String->Number [str]
  "convert string to intereger"
  (let [n (read-string str)]
       (if (number? n) n nil)))

(def get-names
  "Return a vector of all carrier_names in the csv file"
  (??<- [?carrier_name]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)))

(def get-years
  "Return a vector of all years in the csv file"
  (??<- [?year]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)))

(def years 
  "return distinct years"
  (distinct get-years))

; sum the vectors                       
(defbufferop dosum [tuples] [(reduce + (map first tuples))])

(defn delay-by-airline
  "Outputs a vector of carrier name and total delay for that airline"
  [name]
  (??<- [?carrier_name ?total_delay]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)
  (= ?carrier_name name) ;filter predicate
  (String->Number ?arr_del15 :> ?arr_del15_num) ; convert arr_del15 string to int from CSV data file
  (String->Number ?arr_flights :> ?arr_flights_num) ; convert arr_flights string to int
  (dosum ?arr_del15_num :> ?total_delay) ; sum of all delay times
                            ))


<<<<<<< HEAD
(defn flights-by-airline
  "Outputs a vector of carrier name and total flights number for that airline"
  [name]
  (??<- [?carrier_name ?total_flights]
    (flight-data ?line)
    (flight-parser ?line :> ?year _ _ ?carrier_name ?airport
                            _ ?arr_flights ?arr_del15 _ 
                            _ _ _ ?late_aircraft_ct 
                            ?arr_cancelled _ ?arr_delay ?carrier_delay 
                            ?weather_delay _ _ ?late_aircraft_delay 
                            _)
  (= ?carrier_name name) ;filter predicate
  (String->Number ?arr_flights :> ?arr_flights_num) ; convert arr_flights string to int from CSV data file
  (dosum ?arr_flights_num :> ?total_flights) ; sum of all delay times
))
=======
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
>>>>>>> 63238b872f1f1a168bf12b39916e936e6cbcc01a


(defn toVar
  [str] str)

(defn average-by-airline 
  "Outputs a a vector of carrier and and average delay time for that airline"
  [name]
  (??<- [?carrier_name ?average_delay] 
  ((flights-by-airline name) :> ?carrier_name ?flights)
  ((delay-by-airline name) :> ?carrier_name ?delay)
  (/ ?delay ?flights :> ?average_delay)
  ))

(def names 
  (distinct get-names))

; Returns a vector of vectors [carrier_name average_delay]
; my laptop is not able to handle this computation
(defn airline-delay-averages [] [(map average-by-airline names)])

; Format vector to make it work with CSV library
(defn Vector->CSV [vec] (nth vec 0))      

(defn write-to-csv
  "Create a CSV file with the parsed data from delay-by-carrier query"
  []
  (with-open [out-file (clojure.java.io/writer "csv_output/average_delay.csv")]
              (clojure.data.csv/write-csv out-file [
              ["carrier_name" "average_delay"] 
              (Vector->CSV (average-by-airline "Endeavor Air Inc."))
              (Vector->CSV (average-by-airline "American Airlines Inc."))
              (Vector->CSV (average-by-airline "Alaska Airlines Inc."))
              (Vector->CSV (average-by-airline "JetBlue Airways"))
              (Vector->CSV (average-by-airline "Delta Air Lines Inc."))
              ; (Vector->CSV (average-by-airline "ExpressJet Airlines Inc."))
              ; (Vector->CSV (average-by-airline "Frontier Airlines Inc."))
              ; (Vector->CSV (average-by-airline "Allegiant Air"))
              ; (Vector->CSV (average-by-airline "Hawaiian Airlines Inc."))
              ; (Vector->CSV (average-by-airline "Envoy Air"))
              ; (Vector->CSV (average-by-airline "Spirit Air Lines"))
              ; (Vector->CSV (average-by-airline "PSA Airlines Inc."))
              ; (Vector->CSV (average-by-airline "SkyWest Airlines Inc."))
              ; (Vector->CSV (average-by-airline "United Air Lines Inc."))
              ; (Vector->CSV (average-by-airline "Virgin America"))
              ; (Vector->CSV (average-by-airline "Southwest Airlines Co."))
              ; (Vector->CSV (average-by-airline "Mesa Airlines Inc."))
              ; (Vector->CSV (average-by-airline "Republic Airlines"))
              ]
              :quote \-))
)

(defn- parse-strings [^String name]
  (hfs-textline name))

(defn -main
  []
<<<<<<< HEAD
  (println "Starting...")
  ;(println (airline-delay-averages))
  (println "Writing CSV...")
  (write-to-csv) ; creates csv file from parsed data
  (println "DONE")
=======
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
>>>>>>> 63238b872f1f1a168bf12b39916e936e6cbcc01a
  )
