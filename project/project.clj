(defproject project "2.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "https://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"conjars" "http://conjars.org/repo"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cascalog/cascalog-core "2.0.0"] ;; under :dependencies 
                 [org.clojure/data.csv "0.1.4"]               
                 [clj-json "0.5.3"]
                 ]
<<<<<<< HEAD
<<<<<<< HEAD
  :profiles { :provided {:dependencies [[org.apache.hadoop/hadoop-core "1.2.1"]]}
                         :dependencies [[clojure-csv "1.3.0"]]} 
=======
  :dev-dependencies [
                     [org.apache.hadoop/hadoop-core "0.20.2-dev"]
                     [swank-clojure "1.2.1"]]
>>>>>>> 63238b872f1f1a168bf12b39916e936e6cbcc01a
=======
  :dev-dependencies [
                     [org.apache.hadoop/hadoop-core "0.20.2-dev"]
                     [swank-clojure "1.2.1"]]
>>>>>>> 63238b872f1f1a168bf12b39916e936e6cbcc01a
  :jvm-opts ["-Xms768m" "-Xmx768m"]
  :main project.core)

;; allow insecure downloads
(require 'cemerick.pomegranate.aether)
(cemerick.pomegranate.aether/register-wagon-factory!
 "http" #(org.apache.maven.wagon.providers.http.HttpWagon.))
  
