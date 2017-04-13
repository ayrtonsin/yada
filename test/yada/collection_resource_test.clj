;; Copyright © 2014-2017, JUXT LTD.

(ns yada.collection-resource-test
  (:require
   [clj-time.coerce :refer [to-date]]
   [clj-time.core :as time]
   [clojure.edn :as edn]
   [clojure.test :refer :all]
   [ring.mock.request :as mock]
   [ring.util.time :refer [format-date parse-date]]
   [yada.handler :refer [handler]]
   [yada.test-util :refer [to-string]]))

(defn yesterday []
  (time/minus (time/now) (time/days 1)))

(deftest map-resource-test
  (testing "map"
    (let [test-map {:name "Frank"}
          handler (time/do-at (yesterday) (handler test-map))
          request (mock/request :get "/")
          response @(handler request)
          last-modified (some-> response :headers (get "last-modified") parse-date)]

      (is last-modified)
      (is (instance? java.util.Date last-modified))

      (is (= 200 (:status response)))
      (is (= {"content-type" "application/edn"
              "content-length" (str (count (prn-str test-map)))}
             (select-keys (:headers response) ["content-type" "content-length"])))
      (is (instance? java.nio.HeapByteBuffer (:body response)))
      (is (= test-map
             (-> response :body to-string edn/read-string)))

      (let [request (merge (mock/request :get "/")
                           {:headers {"if-modified-since" (format-date (to-date (time/now)))}})
            response @(handler request)]
        (is (= 304 (:status response)))))))
