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

;Helper function to convert String to Integer     
(defn String->Number [str]
  (let [n (read-string str)]
       (if (number? n) n nil)))

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

(defn avg_delay [?airline_name ?arr_delay ?arr_del15] (println (float (/ (String->Number ?arr_delay) (String->Number ?arr_del15)))))

(defn delay-by-carrier 
  "Query outputs all carrier names with corresponding arrival delay time repl usage: (delay-by-carrier)"
  []
  (?<- (stdout) [?carrier_name ?arr_delay ?arr_del15] ;?arr_delay is total delay in minutes ?arr_del15 is total delays over 15 mins per airline
    (flight-data ?line)
    ;(flight-parser ?line :> headers)))
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)))


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
  (println "Starting...")
  (delay-by-carrier) ; outputs carriers carriers and delay times
  (println "Writing CSV...")
  (write-to-csv) ; creates csv file from parsed data
  (println "DONE")
  )
