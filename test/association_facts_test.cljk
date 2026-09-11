(ns association-facts-test
  (:require [clojure.java.io :as io] [clojure.java.shell :as shell]
            [clojure.test :refer [deftest is testing]]
            [kotoba.compiler.core :as compiler] [kotoba.kir :as ir]))
(def source (slurp "src/association_facts.kotoba"))
(defn call [kir f & xs] (ir/execute kir f (vec xs)))
(defn present [x] (when (second x) (nth x 2)))
(def fields ["id" "title" "association" "isic" "country" "kind" "url" "url-provenance"
             "established-date" "last-revised-date" "retrieved-at"])
(def expected
  [{"id" "bap.founding-1949" "title" "BAP founding (About Us)"
    "association" "bap" "isic" "6419" "country" "PHL" "kind" "governance-program"
    "url" "https://bap.org.ph/aboutus.html" "url-provenance" "official-bap-org-ph"
    "established-date" "1949-03-29" "last-revised-date" nil "retrieved-at" "2026-07-16"}
   {"id" "bap.sec-incorporation-1964" "title" "BAP SEC incorporation (About Us)"
    "association" "bap" "isic" "6419" "country" "PHL" "kind" "governance-program"
    "url" "https://bap.org.ph/aboutus.html" "url-provenance" "official-bap-org-ph"
    "established-date" "1964-08-24" "last-revised-date" nil "retrieved-at" "2026-07-16"}])
(deftest reference-preserves-authority
  (let [kir (:kir (compiler/compile-source source :js-kotoba-v1))
        observed (mapv (fn [i] (into {} (map (fn [f] [f (present (call kir 'entry-field "bap" i f))]) fields))) [0 1])]
    (is (= expected observed))
    (is (= ["1949-03-29" "1964-08-24"] (mapv #(present (call kir 'entry-field "bap" % "established-date")) [0 1])))
    (is (= [nil nil] (mapv #(present (call kir 'entry-field "bap" % "last-revised-date")) [0 1])))
    (is (= [["governance"] ["governance"]]
           (mapv (fn [i] (mapv #(present (call kir 'topic "bap" i %)) (range (call kir 'topic-count "bap" i)))) [0 1])))
    (is (= ["bap.founding-1949" "bap.sec-incorporation-1964"]
           (mapv #(present (call kir 'by-topic-id "bap" "governance" %)) [0 1])))
    (is (= #{} (set (:effects kir))))
    (testing "fail closed"
      (is (zero? (call kir 'entry-count "bankers-association-of-the-philippines")))
      (is (zero? (call kir 'entry-count "vnba")))
      (is (nil? (present (call kir 'entry-field "bap" 2 "id"))))
      (is (nil? (present (call kir 'entry-field "bap" 0 "last-revised-date"))))
      (is (nil? (present (call kir 'topic "bap" 1 1))))
      (is (zero? (call kir 'by-topic-count "bap" "labor")))
      (is (nil? (present (call kir 'by-topic-id "bap" "governance" 2)))))))
(defn compiler-root [] (nth (iterate #(.getParent ^java.nio.file.Path %)
  (java.nio.file.Path/of (.toURI (io/resource "kotoba/compiler/core.clj")))) 4))
(defn base64 [x] (.encodeToString (java.util.Base64/getEncoder) x))
(deftest restricted-js-and-wasm-conform-semantically
  (let [js (compiler/compile-source source :js-kotoba-v1) wasm (compiler/compile-source source :wasm32-browser-kotoba-v1)
        js64 (base64 (.getBytes ^String (:source js) "UTF-8")) wasm64 (base64 ^bytes (:bytes wasm))
        p (shell/sh "node" "--input-type=module" "-e"
            (str "import(process.argv[1]).then(async h=>{const j=await import('data:text/javascript;base64," js64 "');const w=await h.instantiateKotoba(Buffer.from(process.argv[2],'base64'));const r=x=>{if(x['entry-field']('bap',0n,'established-date')[2]!=='1949-03-29'||x['entry-field']('bap',1n,'established-date')[2]!=='1964-08-24'||x['entry-field']('bap',0n,'last-revised-date')[1]!==false)throw Error('dates');if(x['by-topic-count']('bap','governance')!==2n||x['by-topic-id']('bap','governance',1n)[2]!=='bap.sec-incorporation-1964'||x['entry-count']('bankers-association-of-the-philippines')!==0n||x['entry-count']('vnba')!==0n)throw Error('authority');};r(j.instantiateKotoba({}));r(w.instance.exports)}).catch(e=>{console.error(e);process.exit(99)})")
            (.toString (.toUri (.resolve (compiler-root) "runtime/browser-host.mjs"))) wasm64)]
    (is (zero? (:exit p)) (str (:out p) (:err p)))))
(deftest production-source-authority
  (is (= ["src/association_facts.kotoba"] (->> (file-seq (io/file "src")) (filter #(.isFile %)) (map str) sort vec))))
