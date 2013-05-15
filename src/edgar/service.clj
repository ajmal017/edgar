(ns edgar.service
    (:require [io.pedestal.service.http :as bootstrap]
              [io.pedestal.service.http.route :as route]
              [io.pedestal.service.http.body-params :as body-params]
              [io.pedestal.service.http.route.definition :refer [defroutes]]
              [ring.util.response :as ring-resp]))

;;
(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s" (clojure-version))))
(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

;;
(defn list-filtered-input
  "List high-moving stocks"
  [])
(defn get-historical-data
  "Get historical data for a particular stock"
  [])
(defn get-streaming-stock-data
  "Get streaming stock data for 1 or a list of stocks"
  [])
(defn stop-streaming-stock-data
  "Stops streaming stock data for 1 or a list of stocks"
  [])


(defroutes routes
  [[
    ["/" {:get home-page}

     ;; Set default interceptors for /about and any other paths under /
     ^:interceptors [(body-params/body-params) bootstrap/html-body]
     ["/about" {:get about-page}]]


    ["/list-filtered-input" {:get list-filtered-input}]
    ["/get-historical-data" {:get get-historical-data}]
    ["/get-streaming-stock-data" {:get get-streaming-stock-data}]
    ["/stop-streaming-stock-data" {:post stop-streaming-stock-data}]
    ]])


;; You can use this fn or a per-request fn via io.pedestal.service.http.route/url-for
(def url-for (route/url-for-routes routes))

;; Consumed by edgar.server/create-server
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; :bootstrap/interceptors []
              ::bootstrap/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::boostrap/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::bootstrap/resource-path "/public"

              ;; Either :jetty or :tomcat (see comments in project.clj
              ;; to enable Tomcat)
              ;;::bootstrap/host "localhost"
              ::bootstrap/type :jetty
              ::bootstrap/port 8080})