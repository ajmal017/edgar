(ns edgar.service-test
  (:use [midje.sweet])
  (:require [clojure.test :refer :all]
            [io.pedestal.service.test :refer :all]
            [io.pedestal.service.http :as bootstrap]
            [edgar.service :as service]
            [edgar.core.edgar :as edgar]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

#_(deftest home-page-test
  (is (=
       (:body (response-for service :get "/"))
       "Hello World!"))
  (is (=
       (:headers (response-for service :get "/"))
       {"Content-Type" "text/html"})))

#_(deftest about-page-test
  (is (.contains
       (:body (response-for service :get "/about"))
       "Clojure 1.5"))
  (is (=
       (:headers (response-for service :get "/about"))
       {"Content-Type" "text/html"})))



;; ... TODO - wait until I understand Midje's asynchronous callback facilities

#_(println "... Grrr[" (response-for service :get "/") "]")
#_(with-state-changes [(before :facts (do 1))]
  (fact ""
        1 => 1
        ))


#_(fact "Test 'list-filtered-input"
      (let [result (edgar/load-historical-data)]
        )
      )

;; ... TODO - create Pedestal service tests (see https://github.com/pedestal/pedestal/blob/master/service/test/io/pedestal/service/http_test.clj)
;;  -- test HTTP method(s)
;;  -- test request inputs
;;  -- test pedestal response value
;;  -- test pedestal response type (edn)

;; ... TODO - create web client tests (see pedestal/app/test/io/pedestal/test/app.clj)
