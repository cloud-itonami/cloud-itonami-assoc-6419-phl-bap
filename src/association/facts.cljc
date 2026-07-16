(ns association.facts
  "Industry rule/history catalog for the Bankers Association of the
  Philippines (BAP) -- a 37th industry-association-level source per
  ADR-2607141700 (cloud-itonami-compliance-fact-federation). The
  SEVENTH entry aligned to ISIC 6419 (other monetary intermediation /
  banking), alongside cloud-itonami-assoc-6419-jpn-zenginkyo (Japan),
  -6419-deu-bankenverband (Germany), -6419-fra-fbf (France),
  -6419-aus-aba (Australia), -6419-are-ubf (UAE), and -6419-vnm-vnba
  (Vietnam) -- the same cross-country-same-ISIC pattern already used
  for ISIC 2910 (VDA/SMMT). Continues this session's Philippines
  research thread alongside cloud-itonami-municipality-phl-manila.

  A WebSearch synthesis had initially conflated two conflicting
  founding years for BAP (1947 vs 1949) across secondary sources --
  resolved by reading bap.org.ph's own aboutus.html page directly,
  which states plainly: 'Established on March 29, 1949, the BAP was
  created to frame rules and regulations in cooperation with the
  Central Bank...' and separately, 'The BAP was officially
  incorporated as a duly Securities and Exchange Commission
  (SEC)-registered corporate entity on August 24, 1964.'

  No Wikidata Q-id was found for BAP specifically -- omitted rather
  than guessed.

  A rule not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "assoc-slug -> vector of self-regulatory rule entries."
  {"bap"
   [{:association-rule/id "bap.founding-1949"
     :association-rule/title "BAP founding (About Us)"
     :association-rule/association "bap"
     :association-rule/isic "6419"
     :association-rule/country "PHL"
     :association-rule/kind :governance-program
     :association-rule/url "https://bap.org.ph/aboutus.html"
     :association-rule/url-provenance :official-bap-org-ph
     :association-rule/established-date "1949-03-29"
     :association-rule/retrieved-at "2026-07-16"
     :association-rule/topic #{:governance}}
    {:association-rule/id "bap.sec-incorporation-1964"
     :association-rule/title "BAP SEC incorporation (About Us)"
     :association-rule/association "bap"
     :association-rule/isic "6419"
     :association-rule/country "PHL"
     :association-rule/kind :governance-program
     :association-rule/url "https://bap.org.ph/aboutus.html"
     :association-rule/url-provenance :official-bap-org-ph
     :association-rule/established-date "1964-08-24"
     :association-rule/retrieved-at "2026-07-16"
     :association-rule/topic #{:governance}}]})

(defn spec-basis [assoc-slug] (get catalog assoc-slug))

(defn coverage
  ([] (coverage (keys catalog)))
  ([slugs]
   (let [have (filter catalog slugs)
         missing (remove catalog slugs)]
     {:requested (count slugs)
      :covered (count have)
      :covered-associations (vec (sort have))
      :missing-associations (vec (sort missing))
      :note (str "cloud-itonami-assoc-6419-phl-bap Wave 0 (ADR-2607141700): "
                 (count (get catalog "bap")) " bap entries seeded with an "
                 "official bap.org.ph citation. Extend "
                 "`association.facts/catalog`, never fabricate a rule id/url.")})))

(defn by-topic [assoc-slug topic]
  (filterv #(contains? (:association-rule/topic %) topic) (spec-basis assoc-slug)))
