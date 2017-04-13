;; Copyright © 2014-2017, JUXT LTD.

(ns yada.put-resource-test
  (:require
   [byte-streams :as b]
   [clojure.test :refer :all]
   [ring.mock.request :refer [request]]
   [yada.handler :refer [handler]]
   [yada.resource :refer [resource]]
   [yada.test-util :refer [to-string]]))

(defn add-headers [request m]
  (merge-with merge request {:headers m}))

(deftest put-test
  (testing "string"
    (let [resource (atom "Bradley")
          h (handler resource)
          response @(h (request :get "/"))
          headers (:headers response)]

      (is (= 200 (:status response)))
      (is (= {"content-length" (str 7)
              "content-type" "text/plain;charset=utf-8"}
             (select-keys headers ["content-length" "content-type"])))
      (is (= "Bradley" (to-string (:body response))))

      (let [response @(h (-> (request :put "/" {:value "Chelsea"})))]
        (is (= 204 (:status response)))
        (is (= (contains? (set (keys (:headers response))) "content-type")))
        (is (= (contains? (set (keys (:headers response))) "content-length")))
        (is (nil? (:body response))))

      (is (= @resource "Chelsea"))

      (let [response @(h (request :get "/"))]
        (is (= 200 (:status response)))
        (is (= {"content-length" (str 7)
                "content-type" "text/plain;charset=utf-8"}
               (select-keys (:headers response) ["content-length" "content-type"])))
        (is (= "Chelsea" (to-string (:body response)))))))

  (testing "return response"
    (let [h (handler (resource {:methods {:put {:response (fn [ctx] (assoc (:response ctx) :body "BODY" :status 200))}}}))
          response @(h (request :put "/"))]
      (is (= "BODY" (b/to-string (:body response))))
      (is (= 200 (:status response)))))

  #_(testing "atom"
      (let [resource (atom {:name "Frank"})
            handler (yada resource)
            request (request :get "/")
            response @(handler request)]

        (given response
               :status := 200
               :headers :> {"content-length" 16
                            "content-type" "application/edn"}
               :body :? string?)

        )))
