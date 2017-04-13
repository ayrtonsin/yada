;; Copyright © 2015, JUXT LTD.

(ns yada.url-resource-test
  (:require
   [yada.resources.url-resource :refer :all]
   [clojure.test :refer :all]
   [clojure.java.io :as io]
   [ring.mock.request :as mock]
   [yada.handler :refer [handler]])
  (:import
   [java.io BufferedInputStream]))

;; Test a single Java resource. Note that this isn't a particularly useful
;; resource, because it contains no knowledge of when it was modified,
;; how big it is, etc. (unless we can infer where it came from, if jar,
;; use the file-size stored in the java.util.zip.JarEntry for
;; content-length.)

(deftest resource-test
  (let [resource (io/resource "static/css/fonts.css")
        handler (handler resource)]
    
    (let [response @(handler (mock/request :get "/"))]
      (is (some? response))
      (is (= 200 (:status response)))
      (is (not (nil? [:headers "content-type"])))
      (is (= "text/css;charset=utf-8" (get-in response [:headers "content-type"])))
      (is (not (nil? [:headers "content-length"])))
      (is (instance? BufferedInputStream (:body response))))))
