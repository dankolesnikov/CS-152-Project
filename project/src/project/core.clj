(ns project.core)
(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[clojure.data.csv :as csv])

(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn- parse-str [^String s]
  (seq (.split s ",")))

; csv data file
(def flight-data (hfs-textline "resources/airline_delay_causes_2018.csv"))

(defn flight-parser 
  "parses csv file"
  [line]
  (map #(.trim %) (first (csv/read-csv line))))

; not working
(defn headers
  "Return headers of csv file; make syntax easier for queries"
  []
  ; ([?year ?month ?carrier ?carrier_name ?airport
  ;                           ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
  ;                           ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
  ;                           ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
  ;                           ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
  ;                           ?empty])
)
  
(defn delay-by-carrier 
  "Query outputs all carrier names with corresponding arrival delay time repl usage: (delay-by-carrier)"
  []
  (?<- (stdout) [?carrier_name ?year ?arr_flights ?arr_delay]
    (flight-data ?line)
    ;(flight-parser ?line :> headers)))
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)))

(def get-names
  (??<- [?carrier_name]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)))

(def get-years
  (??<- [?year]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)))

(def years 
  (distinct get-years))

(def names 
  (distinct get-names))

; Partially working
; Need delay-by-carrier to actually return. What is stdout? Need to change that
; Right now it just executes the job
(defn write-to-csv
  "Create a CSV file with the parsed data from delay-by-carrier query"
  []
  (with-open [out-file (clojure.java.io/writer "carrier_delay.csv")]
              (clojure.data.csv/write-csv out-file [
              ["carrier_name" "arrival_delay"] 
              ["test" "test"]
              [(delay-by-carrier)]
              ]))
)                            

(defn- parse-strings [^String name]
  (hfs-textline name))

(def say-hello
 (fn [name]
   (str "Hello " name)))

(defn print-test 
  []
  (?<- (stdout) [?name]
    (get-names ?name)))


(defn -main
  []
  (println "Avi branch...")
  (println "Starting...")
  (println (distinct names))
  ;(delay-by-carrier) ; outputs carriers carriers and delay times
  (println "Writing CSV...")
  ;(write-to-csv) ; creates csv file from parsed data
  (println "DONE")
  )