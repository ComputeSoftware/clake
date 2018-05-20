# clake

Clojure make.

## Ideas

- Write moar specs
- CLI opts Spec support
- Tasks need to be able to register a cleanup function called on exit. Task functions
should probably have a way of communicating data to the cleanup function.
- Help menu for sub-commands
- Ability to list all tasks registered
- Validate config with Spec
- CircleCI and auto deploy
- show out of date deps
- generate project.clj
- watch filesystem
- when dynamic deps are added to tools-deps we can have task level dependencies
- init task
- set clake-tasks git SHA version on CI