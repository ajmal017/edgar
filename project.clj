(defproject edgar "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :source-paths ["src"]
  :java-source-paths ["src"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [org.clojure/core.incubator "0.1.2"]
                 [jtsclient/jtsclient "9.8.3"]
                 [org.clojure/data.csv "0.1.2"]
                 #_[com.datomic/datomic "0.8.3335"]
                 [storm "0.9.0-wip16"]
                 [org.twitter4j/twitter4j-core "2.2.6-SNAPSHOT"]
                 [org.twitter4j/twitter4j-stream "2.2.6-SNAPSHOT"]
                 ]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler edgar.handler/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}}
  :repositories { "local" ~(str (.toURI (java.io.File. (str (-> (load-file "etc/config.clj") :dev :root-dir) ".m2/repository/")))) 
                  "twitter4j" "http://twitter4j.org/maven2"
                }
  )
