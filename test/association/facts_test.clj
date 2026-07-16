(ns association.facts-test
  (:require [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest bap-has-spec-basis
  (let [sb (facts/spec-basis "bap")]
    (is (= 2 (count sb)))
    (is (every? #(= "6419" (:association-rule/isic %)) sb))
    (is (every? #(= "PHL" (:association-rule/country %)) sb))))

(deftest unknown-association-has-no-spec-basis
  (is (nil? (facts/spec-basis "vnba")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["bap" "vnba"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["vnba"] (:missing-associations c)))))

(deftest by-topic-filters
  (is (= 2 (count (facts/by-topic "bap" :governance))))
  (is (empty? (facts/by-topic "bap" :labor)))
  (is (empty? (facts/by-topic "vnba" :governance))))
