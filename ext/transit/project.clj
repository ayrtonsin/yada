;; Copyright © 2014-2017, JUXT LTD.

(def VERSION "1.2.1")

(defproject yada/transit VERSION
  :pedantic? :abort
  :dependencies [[yada/core ~VERSION]
                 [com.cognitect/transit-clj "0.8.297"]])
