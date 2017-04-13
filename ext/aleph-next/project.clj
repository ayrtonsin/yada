;; Copyright © 2014-2017, JUXT LTD.

(def VERSION "1.2.1")

(defproject yada/aleph-next VERSION
  :pedantic? :abort
  :dependencies [[aleph "0.4.2-alpha8"]
                 [manifold "0.1.6-alpha1"]
                 [yada/core ~VERSION]])
