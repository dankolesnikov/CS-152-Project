(ns project.core)
(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[clojure.string :as str])
(use 'cascalog.api)
(use 'cascalog.playground) (bootstrap)

(defn- parse-str [^String s]
  (seq (.split s ",")))

(defn longify [str]
  (Long/parseLong str))

(defmapcatop split [sentence]
    (seq (.split sentence "\\s+")))


(def reaction-data
  (let [source (hfs-textline "/home/jason/Downloads/CS152/gitproject/CS-152-Project/project/resources/airline_delay_causes.csv")]
    (<- [?line]
        (source ?line))
    ))

;(def reaction-data
;  (let [source (hfs-textline "/home/jason/Downloads/CS152/gitproject/CS-152-Project/project/resources/airline_delay_causes.csv")]
;   (<- [?reaction ?to]
;       (source ?line)
;       (parse-str ?line :> _ _ _ ?reaction ?to _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _)
;       (:distinct false))
;   ))

(comment
  (defnk first-n
    [gen n :sort nil :reverse false]
    (let [in-fields (get-out-fields gen)
          out-vars  (v/gen-nullable-vars (count in-fields))]
      (<- out-vars
          (gen :>> in-fields)
          (:sort :<< (if sort (collectify sort)))
          (:reverse reverse)
          (c/limit [n] :<< in-fields :>> out-vars))))
  )

(defn test1
  ([]     (test1 "Hello world!"))
  ([msg]  (println msg)))

(defn -main
  []
  ;(println (parse-str "\"year\",\" month\",\"carrier\",\"carrier_name\",\"airport\",\"airport_name\",\"arr_flights\",\"arr_del15\",\"carrier_ct\",\" weather_ct\",\"nas_ct\",\"security_ct\",\"late_aircraft_ct\",\"arr_cancelled\",\"arr_diverted\",\" arr_delay\",\" carrier_delay\",\"weather_delay\",\"nas_delay\",\"security_delay\",\"late_aircraft_delay\","))
  (println (reaction-data 100))
  ;(test1)
  ;(println (io/resource "airline_delay_causes.csv"))
  ;(?- (stdout)sentence)
  )
