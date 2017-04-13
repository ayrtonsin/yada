;; Copyright © 2014-2017, JUXT LTD.

(ns yada.nil-test
  (:require
   [clojure.test :refer :all]
   [ring.mock.request :refer [request]]
   [yada.handler :refer [handler]]))

(deftest nil-test
  (testing "A nil resource should yield on nil"
    (doseq [method [:get :post :put]]
      (let [res (handler nil)]
        (try
          @(res (request method "/"))
          (catch clojure.lang.ExceptionInfo e
            (is (= {:error {:status 404}} (ex-data e)))))))))
