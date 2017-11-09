# Ship

🚀 Data app of the future

## What is it?

Ship is a reactive tree editor dressed up as a general-purpose computing environment with a visual programming interface. It's inspired by Smalltalk, Emacs, Forth, and of course Clojure (which was chosen for this implementation) as well as a [tediously long list](https://en.wikipedia.org/wiki/Visual_programming_language) [of](https://en.wikipedia.org/wiki/K_(programming_language)) [other](https://en.wikipedia.org/wiki/Elm_(programming_language)) [similar](https://en.wikipedia.org/wiki/Microsoft_Excel) [works](https://en.wikipedia.org/wiki/Sequent_calculus). Ship's core is a message-oriented [truth maintenance system](https://en.wikipedia.org/wiki/Reason_maintenance), and the rest aims to be a convenient and pleasant mechanism for interacting with this system. There are [some](https://en.wikipedia.org/wiki/Partial_evaluation#Futamura_projections) [other](https://en.wikipedia.org/wiki/Fold_(higher-order_function)#Universality) [interesting](https://en.wikipedia.org/wiki/Rete_algorithm) [phenomena](https://en.wikipedia.org/wiki/Fractal) [related](https://en.wikipedia.org/wiki/Paraconsistent_logic) to Ship as well.

Ship runs in mobile and desktop browsers today, but can be straightforwardly ported to most platforms.

The goal of this project is to promote and amplify computational thinking.

It is designed to turn even the most casual user into a computational thinker, and to be delightful and useful enough that casual users will become habitual ones.

See [some non-technical marketing material](marketing-material.md) (WIP) for an approximation of the final vision.

## How does it work?

A running instance of Ship is a message loop with some listeners ("processes") whose states are incrementally updated with each message, and who can examine their own and each other's current state while evaluating messages. Process states are algebraic structures represented as [factfold](https://github.com/notduncansmith/factfold) models. This representation is convenient because of its programmatic composability.

## What is the project status?

The project is designed to progress in phases.

Phase 1 will produce a stable reference implementation with well-designed extension mechanisms. It should be pleasant to use for small scripting tasks and reasonably efficient.

Phase 2 will add examples and extensions to promote interoperability and usability. In other words, connect to tools & services that people care about, make the app sleeker and more forgiving, and design good onboarding/support experiences.

Phase 3 will expand to other platforms with emphasis on performance and collaboration/publishing workflows. This includes developing specialized runtimes, and investing in extensions that support professional use.

Today, we are in Phase 1. The codebase is unstable and the editor itself is a work in progress.

Phase 1 features remaining:

- [ ] Data structure editor (Boolean, Number, String, Vector, Map, CLJS)

- [ ] Local/remote durable state backup (aka "saving things")

- [ ] Public extension repository index and website (something simple running on GH)

- [ ] Clojure namespace process for exploring

- [ ] Tab overview/selection screen

- [ ] Process selector modal

- [ ] Drag-and-drop: box element reordering, send to process, carry while zooming (like spring-loaded folders)

- [ ] Better text editor than `textarea` (maybe [CodeMirror](https://codemirror.net)?)

- [ ] Fuzzy search

## License

Licensed under the Eclipse Public License, same as Clojure.
