(ns jj.majavat.ttl.builder-test
  (:require [clojure.test :refer [deftest is]]
            [jj.majavat.parser :as parser]
            [jj.majavat.ttl.builder :as ttl]
            [mock-clj.core :as mock])
  (:import (java.util.concurrent TimeUnit)))

(deftest ttl-test
  (mock/with-mock
    [parser/parse [{:type  :text
                    :value "text"}]]
    (let [file-path "input.txt"
          render-fn (ttl/build-ttl-renderer file-path {:ttl      1
                                                       :type     TimeUnit/SECONDS})]
      (is (= "text" (render-fn {}))))

    (Thread/sleep 4000)
    (is (or
          (= 5 (mock/call-count parser/parse))
          (= 6 (mock/call-count parser/parse))))
    (mock/reset-calls! parser/parse)))
