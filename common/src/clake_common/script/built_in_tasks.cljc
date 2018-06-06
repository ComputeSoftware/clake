(ns clake-common.script.built-in-tasks)

;; TODO: Ideally this would be auto-generated
(def default-refer-tasks
  '{repl    clake-tasks.repl/repl
    test    clake-tasks.test/test
    uberjar clake-tasks.uberjar/uberjar})

(def built-in-tasks (into #{} (vals default-refer-tasks)))

(def cli-task-help-option ["-h" "--help" "Print help menu for this task."])

(defn built-in?
  "Returns true if `qualified-task` is a built in task."
  [qualified-task]
  (get built-in-tasks qualified-task))