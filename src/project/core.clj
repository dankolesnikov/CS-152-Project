(ns project.core
  (:gen-class)) ; needs to be here to work with hadoop
(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[clojure.data.csv :as csv])

(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

; csv data file
(def flight-data (hfs-textline "resources/airline_delay_causes_2012_2017.csv"))

(defn flight-parser 
  "Pparses csv file"
  [line]
  (map #(.trim %) (first (csv/read-csv line))))

(defn String->Number [str]
  "Convert a string to an int"
  (let [n (read-string str)]
       (if (number? n) n nil)))

; Convert vector to string
(defn Vector->String [vec] (nth vec 0))  

(defn- parse-strings [^String name]
  (hfs-textline name))

(defn get-names-all
  [year]
  "Return a vector of all carrier names for the specified year"
  (??<- [?carrier_name]
    (flight-data ?line)
    (flight-parser ?line :> ?year ?month ?carrier ?carrier_name ?airport
                            ?airport_name ?arr_flights ?arr_del15 ?carrier_ct 
                            ?weather_ct ?nas_ct ?security_ct ?late_aircraft_ct 
                            ?arr_cancelled ?arr_diverted ?arr_delay ?carrier_delay 
                            ?weather_delay ?nas_delay ?security_delay ?late_aircraft_delay 
                            ?empty)
    (= year ?year)))

(defn get-names-all-distinct 
  "Return a vector of unique "
  [year]
  (distinct (get-names-all year))
)

(defn get-years-all
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

(defn get-years-all-distinct 
  []
  (distinct (get-years-all))
)


(def years 
  "return distinct years"
  (distinct get-years-all))

; sum the vectors                       
(defbufferop dosum [tuples] [(reduce + (map first tuples))])

(defn percentage-delay-by-airline
  "Outputs a vector of carrier name and total percentage of delayed flights for that airline"
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
  [name year]
  "
  Args:
    name: carrier name
    year: year of interest 
  Return:
    Vector of carrier name and total number of flights took place in the given year"
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

(defn delay-percentage-by-airline 
  [name year]
  "
  Args: 
    name: carrier name
    year: year of interest 
  Return:
    Vector of carrier name and percentage of delayed flights that year"
  (??<- [?carrier_name ?average_delay_time] 
  ((flights-by-airline name year) :> ?carrier_name ?flights)
  ((percentage-delay-by-airline name year) :> ?carrier_name ?delay)
  (/ ?delay ?flights :> ?average_delay_time_number)
  (* 100 ?average_delay_time_number :> ?average_delay_time) ; scaling
  ))    

; Returns a vector of vectors [carrier_name average_delay]
(defn airline-delay-percentages [year] (map Vector->String (map delay-percentage-by-airline (get-names-all-distinct year) (repeat year))) )

(defn airline-delay-percentages-by-carrier
  [name year]
  "
  Args: 
    name: carrier name
    year: year of interest 
  Return:
    Vector of year and percentage of delayed flights that year for the carrier
  "
  (println (str (str "Percentage of delays for airline: " name) (str " For Year: " (first year))))
  (vector (str (first year)) (first (first (??<- [?percentage_delay]
    ((delay-percentage-by-airline name year) :> _ ?percentage_delay))
  )))
)

(defn airline-delay-percentages-by-carrier-all-years
  [name]
  (map airline-delay-percentages-by-carrier (repeat name) (get-years-all-distinct))
)

(defn write-csv-by-airline
  [name]
  (with-open [out-file (clojure.java.io/writer (str "results/percentage_delayed_flights_" name ".csv"))]
              (clojure.data.csv/write-csv out-file
              (cons ["Year" "Percentage of Delayed Flights"]
              (airline-delay-percentages-by-carrier-all-years name))
              :quote \-)
  )
)

(defn write-csv-by-year
  "Create a CSV file for average delay time of all airline in the specific year"
  [year]
  (with-open [out-file (clojure.java.io/writer (str "results/percentage_delayed_flights_" (Vector->String year) ".csv"))]
              (clojure.data.csv/write-csv out-file
              (cons ["Carrier" "Percentage of Delayed Flights"]
              (airline-delay-percentages (first (first year)))
              :quote \-))
  )
)

(defn write-all-by-year
  "Perform write for all years"
  []
  (dorun (map #(write-csv-by-year %) (get-years-all-distinct))))

(defn -main
  []
  (println "Starting ...")
  (println "Writing CSV ...")
  (write-csv-by-airline "American Airlines Inc.")
  (println "DONE!")
)