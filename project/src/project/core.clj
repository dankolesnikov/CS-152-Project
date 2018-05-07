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
  [name year]
  (??<- [?carrier_name ?total_delay]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)
  (not= ?arr_del15 "")
  (not= ?arr_flights "")
  (= year ?year)
  (= ?carrier_name name) ;filter predicate
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
  (not= ?arr_del15 "")
  (not= ?arr_flights "")
  (= year ?year)
  (= ?carrier_name name) ;filter predicate
  (String->Number ?arr_flights :> ?arr_flights_num) ; convert arr_flights string to int from CSV data file
  (dosum ?arr_flights_num :> ?total_flights) ; sum of all delay times
))


(defn toVar
  [str] str)

(defn average-by-airline 
  "Outputs a a vector of carrier and and average delay time for that airline"
  [name year]
  (??<- [?carrier_name ?average_delay] 
  ((flights-by-airline name year) :> ?carrier_name ?flights)
  ((delay-by-airline name year) :> ?carrier_name ?delay)
  (/ ?delay ?flights :> ?average_delay)
  ))

(def names 
  (distinct (get-names "2018")))

; Returns a vector of vectors [carrier_name average_delay]
; my laptop is not able to handle this computation
(defn airline-delay-averages [year] (map average-by-airline (get-names-distinct year) (repeat year)))

; Format vector to make it work with CSV library
(defn Vector->CSV [vec] (nth vec 0))      
   

(defn gen-vectors
  [year]
  (??<-[?lst]
  (names ?carrier_name)
  ((average-by-airline ?carrier_name year) :. ?lst)
  )
  )



(defn write-to-csv
  "Create a CSV file with the parsed data from delay-by-carrier query"
  [year]
  (with-open [out-file (clojure.java.io/writer "csv_output/average_delay.csv")]
              (clojure.data.csv/write-csv out-file [
              ["carrier_name" "average_delay"]
              (Vector->CSV (average-by-airline "Endeavor Air Inc." year))
              (Vector->CSV (average-by-airline "American Airlines Inc." year))
              (Vector->CSV (average-by-airline "Alaska Airlines Inc." year))
              (Vector->CSV (average-by-airline "JetBlue Airways" year))
              (Vector->CSV (average-by-airline "Delta Air Lines Inc." year))
              (Vector->CSV (average-by-airline "ExpressJet Airlines Inc." year))
              (Vector->CSV (average-by-airline "Frontier Airlines Inc." year))
              (Vector->CSV (average-by-airline "Allegiant Air" year))
              (Vector->CSV (average-by-airline "Hawaiian Airlines Inc." year))
              (Vector->CSV (average-by-airline "Envoy Air" year))
              (Vector->CSV (average-by-airline "Spirit Air Lines" year))
              (Vector->CSV (average-by-airline "PSA Airlines Inc." year))
              (Vector->CSV (average-by-airline "SkyWest Airlines Inc." year))
              (Vector->CSV (average-by-airline "United Air Lines Inc." year))
              (Vector->CSV (average-by-airline "Virgin America" year))
              (Vector->CSV (average-by-airline "Southwest Airlines Co." year))
              (Vector->CSV (average-by-airline "Mesa Airlines Inc." year))
              (Vector->CSV (average-by-airline "Republic Airlines" year))
              ]
              :quote \-))
)

(defn- parse-strings [^String name]
  (hfs-textline name))

(defn -main
  []
  (println "Starting...")
  ;(println (airline-delay-averages))
  (println "Writing CSV...")
  ;(with-open [out-file (clojure.java.io/writer "csv_output/average_delay.csv")]
  ;            (clojure.data.csv/write-csv out-file (gen-vectors allnames "2017")
  ;            :quote \-))
  ;(println "Endeavor Air Inc.")
  (println (airline-delay-averages "2018"))
  ;(println (gen-vectors "2018"))
  ;(println (gen-vectors "2018"))
  ;(write-to-csv "2018") ; creates csv file from parsed data
  (println "DONE")
  )
