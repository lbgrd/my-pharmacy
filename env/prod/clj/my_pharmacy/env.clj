(ns my-pharmacy.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[my-pharmacy started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[my-pharmacy has shut down successfully]=-"))
   :middleware identity})
