(ns stagistry.middleware
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.json :as json-middleware]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn wrap-middleware [handler]
  (-> handler
      (wrap-defaults api-defaults)
      wrap-exceptions
      (json-middleware/wrap-json-body {:keywords? true})
      json-middleware/wrap-json-response
      wrap-reload))
