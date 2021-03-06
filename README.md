# Shoe

[![CircleCI](https://circleci.com/gh/ComputeSoftware/shoe.svg?style=svg)](https://circleci.com/gh/ComputeSoftware/shoe)

**Cl**ojure m**ake** - a [simple](https://www.youtube.com/watch?v=F0Lv53lop2g) Clojure build tool, based on tools-deps (deps.edn).

## Rationale

When building software, you shouldn't have to think about your build tool differently than how you think about your application. All you need to do is write some functions that perform your build steps. And there is no reason to couple these functions to a build framework (à la Leiningen & Boot). 

Clojure has always had the ability to execute Clojure code from the command line via `clojure.main`. The reason this approach never took off (until now) was because you needed to determine the classpath to start the JVM. This required that you pull in some sort of Maven dependency resolver (i.e. [Pomegranate](https://github.com/cemerick/pomegranate)) or, more likely, fall back to the existing tools that solve this problem - Leiningen or Boot. To address this missing piece, the Clojure team has released [tools-deps](https://github.com/clojure/tools.deps.alpha). Tools-deps has a narrow, well-defined goal -- classpath creation. 

The [Clojure CLI](https://clojure.org/guides/deps_and_cli) combines `clojure.main` functionality with tools-deps classpath creation, making running Clojure programs easy. It still felt like there was a missing piece, however. I'd like to be able to run arbitrary Clojure functions from the command line, not just a `-main`. You can absolutely do this with the Clojure CLI, however it requires a few extra options and writing some code in the command line. Further, I'd like my functions to parse command line arguments without needing to write the boilerplate code that [tools.cli](https://github.com/clojure/tools.cli#example-usage) requires. It'd also be great if I could build up a library of common functions that I can use across all my projects. And as soon as you have a set of general, reusable functions, you want a way to pass default, project-specific options to these functions. Thus the creation of shoe (name inspired by [Rake](https://github.com/ruby/rake)).


At its core, shoe is simply a collection of functions that can be easily run using the Clojure CLI. shoe provides an optional CLI to make running functions easier.

## Clojure CLI Usage

All the built in functions are located in the `tasks` directory and contained within their own folder. For example, the `repl` task source code is located at `tasks/repl`. The code is structured in this way to make running from the Clojure CLI easy. This is best shown by example. Let's launch a REPL using the `repl` task.

### Starting a REPL with Clojure CLI

```bash
$ clojure -Sdeps '{:deps {shoe-tasks.repl {:git/url "https://github.com/ComputeSoftware/shoe" :sha "873e1a2e50a9dd961a0a251a12aed9e13b538416" :deps/root "tasks/repl"}}}' -m shoe-tasks.repl
nREPL server started on port 45023
```

That's a bit nasty to type everytime you want to start a REPL. Let's make this easier with an alias! Create a `deps.edn` file in your current directory (or edit your user `deps.edn` file located at `~/.clojure/deps.edn`) and add the below alias:

```clojure
{:aliases {:repl {:extra-deps {shoe-tasks.repl {:git/url   "https://github.com/ComputeSoftware/shoe"
                                                 :sha       "873e1a2e50a9dd961a0a251a12aed9e13b538416"
                                                 :deps/root "tasks/repl"}}
                  :main-opts  ["-m" "shoe-tasks.repl"]}}}
```

This makes starting a REPL short and sweet.

```bash
$ clojure -A:repl
nREPL server started on port 34093
```

The same technique can be used for all of the tasks in the `tasks` directory. Note that `:deps/root` will need to be changed to the correct relative Git directory when working with other tasks. For example, the `test` task would have `:deps/root` set to `tasks/test`.

You can see all of the options that a task supports by using the `--help` flag. 

```bash
$ clojure -A:repl --help
...
```

At this point the aformentioned problem should be clear -- we need a way to minimize the amount of configuration and to set default options for tasks within the scope of a project. This is where the shoe CLI comes in. 

## Shoe CLI Usage

The shoe CLI is written in ClojureScript and available as a NPM module.

### Installation & Upgrading

To install or upgrade shoe, run this command.

```bash
npm install -g shoe-cli
```

### Starting a REPL with shoe

Remember all the [configuration](#starting-a-repl-with-clojure-cli) we needed to do to start a REPL via the Clojure CLI? shoe does all the automatically, exposing the function call as a command line task. 

```bash
$ shoe repl
nREPL server started on port 36389
```

In the background shoe is parsing the command line arguments passed to it, resolving the string `"repl"` to an actual function call, and passing parsed command line options to that resolved function (it executes the function with  `clojure` in a sub-process, isolating dependencies on the task level). For those curious, dive deeper in the [implementation details](#implementation-details) section.

As you saw with the launching a REPL using the Clojure CLI, you're also able to print a help menu for tasks via the shoe CLI.

```bash
$ shoe repl --help
```

All tasks, built-in and user defined, always have a help menu.

### Basic Usage

```bash
shoe repl # Launch a nREPL

shoe uberjar # package the project and dependencies as standalone jar

shoe test # run all tests in the project
```

### Custom tasks

TODO: write this

### Configuration

shoe's configuration is stored in `shoe.edn` which is loaded from the directory where you launched `shoe`.


### Implementation Details

TODO: write this

## TODO

- Write moar specs
- CLI opts Spec support
- Tasks need to be able to register a cleanup function called on exit. Task functions
should probably have a way of communicating data to the cleanup function.
- Ability to list all tasks registered
- Validate config with Spec
- when dynamic deps are added to tools-deps we can have task level dependencies
- use color in console printing

### Tasks

- build a thin jar
- install a jar into local m2
- push to a remote repo
- watch filesystem
- show out of date deps
- create new project
- lein javac