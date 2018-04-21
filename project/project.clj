(defproject project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cascalog/cascalog-core "3.0.0"]
                 [org.apache.hadoop/hadoop-core "1.2.1"]
                 [org.clojure/data.csv "0.1.4"]
                 ]
  :jvm-opts ["-Xms768m" "-Xmx768m"]
  :main project.core)
  
