;; Copyright © 2014-2017, JUXT LTD.

(ns yada.form-test
  (:require
   [byte-streams :as bs]
   [clojure.edn :as edn]
   [clojure.test :refer :all]
   [ring.mock.request :as mock]
   [schema.core :as s]
   [yada.handler :refer [handler]]
   [yada.resource :refer [resource]]))

(deftest post-test
  (let [h (handler
           (resource
            {:methods
             {:post {:parameters {:form {:foo s/Str}}
                     :consumes "application/x-www-form-urlencoded"
                     :response (fn [ctx]
                                 (pr-str (:parameters ctx)))}}}))]

    ;; Nil post body
    (let [response @(h (mock/request :post "/"))]
      (is (= 200 (:status response)))
      (is (= {} (edn/read-string (bs/to-string (:body response))))))

    ;; Form post body
    (let [response @(h (mock/request :post "/"
                                     {"foo" "bar"}))]
      (is (= 200 (:status response)))
      (is (= {:form {:foo "bar"}} (edn/read-string (bs/to-string (:body response))))))))

;; Need to test where strings are used rather than keywords

(deftest post-test-with-query-params
  (let [h (handler
           (resource
            {:methods
             {:post {:parameters {:query {:foo s/Str}
                                  :form {:bar s/Str}}
                     :consumes "application/x-www-form-urlencoded"
                     :response (fn [ctx] (pr-str (:parameters ctx)))}}}))]

    ;; Nil post body
    (let [response @(h (mock/request :post "/?foo=123"))]
      (is (= 200 (:status response)))
      (is (= {:query {:foo "123"}} (edn/read-string (bs/to-string (:body response))))))

    ;; Form post body
    (let [response @(h (mock/request :post "/?foo=123"
                                     {"bar" "456"}))]
      (is (= 200 (:status response)))
      (is (=  {:query {:foo "123"}
               :form {:bar "456"}} (edn/read-string (bs/to-string (:body response))))))))
