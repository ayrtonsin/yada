;; Copyright © 2015, JUXT LTD.

(ns yada.response-for-test
  (:require
   [clojure.test :refer :all]
   [yada.test :refer [response-for]]
   [yada.handler :refer [handler]]))

(deftest response-for-test
  (let [res (response-for ["/" (handler "foo")] :get "/")]
    (is (= 200 (:status res)))
    (is (= "3" (get-in res [:headers "content-length"])))
    (is (= "text/plain;charset=utf-8" (get-in res [:headers "content-type"])))
    (is (= "accept-charset" (get-in res [:headers "vary"])))
    (is (= "foo" (:body res)))))

;; TODO: Test all other options
