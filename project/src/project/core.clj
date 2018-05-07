(ns project.core
  (:gen-class)) ; needs to be here to work with hadoop
(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[clojure.data.csv :as csv])

(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

; csv data file
(def flight-data (hfs-textline "https://s3.us-east-2.amazonaws.com/cs152jar/airline_delay_causes_2012_2017.csv"))

(defn flight-parser 
  "parses csv file"
  [line]
  (map #(.trim %) (first (csv/read-csv line))))

(defn String->Number [str]
  "convert string to intereger"
  (let [n (read-string str)]
       (if (number? n) n nil)))

(defn get-names
  [year]
  "Return a vector of all carrier_names in the csv file"
  (??<- [?carrier_name]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)
    (= year ?year)))

(defn get-names-distinct 
  [year]
  (distinct (get-names year))
)

(defn get-years
  "Return a vector of all years in the csv file"
  []
  (??<- [?year]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)))

(defn get-years-distinct 
  []
  (distinct (get-years))
)


(def years 
  "return distinct years"
  (distinct get-years))

; sum the vectors                       
(defbufferop dosum [tuples] [(reduce + (map first tuples))])

(defn delay-by-airline
  "Outputs a vector of carrier name and total delay for that airline"
  [name year]
  (??<- [?carrier_name ?total_delay]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)
  (not= ?arr_del15 "") ; validation
  (not= ?arr_flights "") ; validation
  (= year ?year) ; filter by year
  (= ?carrier_name name) ;filter by name
  (String->Number ?arr_del15 :> ?arr_del15_num) ; convert arr_del15 string to int from CSV data file
  (String->Number ?arr_flights :> ?arr_flights_num) ; convert arr_flights string to int
  (dosum ?arr_del15_num :> ?total_delay) ; sum of all delay times
                            ))


(defn flights-by-airline
  "Outputs a vector of carrier name and total flights number for that airline"
  [name year]
  (??<- [?carrier_name ?total_flights]
    (flight-data ?line)
    (flight-parser ?line :> ?year _ _ ?carrier_name ?airport
                            _ ?arr_flights ?arr_del15 _ 
                            _ _ _ ?late_aircraft_ct 
                            ?arr_cancelled _ ?arr_delay ?carrier_delay 
                            ?weather_delay _ _ ?late_aircraft_delay 
                            _)
  (not= ?arr_del15 "") ; validation
  (not= ?arr_flights "") ; validation
  (= year ?year) ; filter by year 
  (= ?carrier_name name) ;filter by carrier name
  (String->Number ?arr_flights :> ?arr_flights_num) ; convert arr_flights string to int from CSV data file
  (dosum ?arr_flights_num :> ?total_flights) ; sum of all delay times
))


(defn average-by-airline 
  "Outputs a a vector of carrier and and average delay time for that airline"
  [name year]
  (??<- [?carrier_name ?average_delay] 
  ((flights-by-airline name year) :> ?carrier_name ?flights)
  ((delay-by-airline name year) :> ?carrier_name ?delay)
  (/ ?delay ?flights :> ?average_delay) ; compute the average delay 
  ))

; Format vector to make it work with CSV library
(defn Vector->CSV [vec] (nth vec 0))

; Convert vector to string
(defn Vector->String [vec] (nth vec 0))      

; Returns a vector of vectors [carrier_name average_delay]
(defn airline-delay-averages [year] (map Vector->CSV (map average-by-airline (get-names-distinct year) (repeat year))) )

(defn write
  "Outputs a CSV file for average delay time of all airline in the specific year"
  [year]
  (with-open [out-file (clojure.java.io/writer (str "csv_output/average_delays_" (Vector->String year) ".csv"))]
              (clojure.data.csv/write-csv out-file (airline-delay-averages year)
              :quote \-))
  )

(defn write-all
  []
  (dorun (map #(write %) (get-years-distinct)))
  )


; For legacy
; (defn write-to-csv
;   "Create a CSV file with the parsed data from delay-by-carrier query"
;   [year]
;   (with-open [out-file (clojure.java.io/writer "csv_output/average_delay.csv")]
;               (clojure.data.csv/write-csv out-file [
;               ["carrier_name" "average_delay"]
;               (Vector->CSV (average-by-airline "Endeavor Air Inc." year))
;               (Vector->CSV (average-by-airline "American Airlines Inc." year))
;               (Vector->CSV (average-by-airline "Alaska Airlines Inc." year))
;               (Vector->CSV (average-by-airline "JetBlue Airways" year))
;               (Vector->CSV (average-by-airline "Delta Air Lines Inc." year))
;               (Vector->CSV (average-by-airline "ExpressJet Airlines Inc." year))
;               (Vector->CSV (average-by-airline "Frontier Airlines Inc." year))
;               (Vector->CSV (average-by-airline "Allegiant Air" year))
;               (Vector->CSV (average-by-airline "Hawaiian Airlines Inc." year))
;               (Vector->CSV (average-by-airline "Envoy Air" year))
;               (Vector->CSV (average-by-airline "Spirit Air Lines" year))
;               (Vector->CSV (average-by-airline "PSA Airlines Inc." year))
;               (Vector->CSV (average-by-airline "SkyWest Airlines Inc." year))
;               (Vector->CSV (average-by-airline "United Air Lines Inc." year))
;               (Vector->CSV (average-by-airline "Virgin America" year))
;               (Vector->CSV (average-by-airline "Southwest Airlines Co." year))
;               (Vector->CSV (average-by-airline "Mesa Airlines Inc." year))
;               (Vector->CSV (average-by-airline "Republic Airlines" year))
;               ]
;               :quote \-))
; )

(defn- parse-strings [^String name]
  (hfs-textline name))

(defn -main
  []
  (println "Starting ...")
  (println "Writing CSV ...")
  (write-all)
  (println "DONE!")
  )

