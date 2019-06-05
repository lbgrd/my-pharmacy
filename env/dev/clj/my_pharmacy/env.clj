(ns my-pharmacy.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [my-pharmacy.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[my-pharmacy started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[my-pharmacy has shut down successfully]=-"))
   :middleware wrap-dev})
