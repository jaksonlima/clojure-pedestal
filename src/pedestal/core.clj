(ns pedestal.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [clojure.data.json :as json]
            [io.pedestal.http.content-negotiation :as content-negotiation])
  (:use [clojure.pprint]))

(def supported-types ["text/html"
                      "application/edn"
                      "application/json"
                      "text/plain"])

(def content-negotiation-interceptor
  (content-negotiation/negotiate-content supported-types))

(def coerce-body-interceptor
  {:name ::coerce-body
   :leave
   (fn [context]
     (let [accepted (get-in context [:request :accept :field] "text/plain")
           response (get context :response)
           body (get response :body)
           coerced-body (case accepted
                          "text/html" body
                          "text/plain" body
                          "application/edn" (pr-str body)
                          "application/json" (json/write-str body))
           updated-response (assoc response
                              :headers {"Content-Type" accepted}
                              :body coerced-body)]
       (assoc context :response updated-response)))})

(defn home-page
  [_request]
  {:status 200 :body "Hello, World!"})

(defn all-page
  [_request]
  {:status  200
   :body    {:id 123 :type "loaded"}
   :headers {"Content-Type" "application/json"}})

(def routes
  (route/expand-routes
    #{
      ["/hello" :get [coerce-body-interceptor
                      content-negotiation-interceptor
                      home-page]
       :route-name :home-page]
      ["/all" :get [coerce-body-interceptor
                    content-negotiation-interceptor
                    all-page]
       :route-name :all]
      }
    )
  )

(def service {:env                 :prod
              ::http/routes        routes
              ::http/resource-path "/"
              ::http/type          :jetty
              ::http/port          8089})

(defn start []
  (http/start (http/create-server service)))

;(def -main (start))

(defn -main [] (start))




