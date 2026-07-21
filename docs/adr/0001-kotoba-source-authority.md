# ADR 0001: Kotoba is the BAP catalog source authority

- Status: Accepted
- Date: 2026-07-21

`src/association_facts.kotoba` is the sole production source. It preserves the
official complete 1949-03-29 founding and 1964-08-24 incorporation dates, keeps
both revision dates absent, and does not import the conflicting secondary-source
1947 claim. Both entries retain their governance topic and official BAP citation.
Unknown associations, aliases, fields, topics, and indexes fail closed; no
effects are declared.

Conformance is observable semantics across the reference evaluator, restricted
JavaScript, and instantiated typed WebAssembly, including the typed ABI, bounds,
effects, and rejection behavior. Compiler-output byte identity is not a language
gate. Clojure and the JVM are compiler/test hosts only.
