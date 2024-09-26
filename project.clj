(defproject pedestal "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [io.pedestal/pedestal.service "0.7.0"]
                 [io.pedestal/pedestal.jetty "0.7.0"]
                 [org.slf4j/slf4j-simple "2.0.10"]
                 [org.clojure/data.json "2.5.0"]]
  :repl-options {:init-ns pedestal.core})
