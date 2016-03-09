(ns stagistry.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [stagistry.middleware :refer [wrap-middleware wrap-api-middleware]]
            [stagistry.query :refer [create-piece all-pieces]]
            [clojure.pprint :refer [pprint]]
            [environ.core :refer [env]]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(def loading-page
  (html5
   [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))]
    [:body
     mount-target
     (include-js "/js/app.js")]))



(defroutes routes
  (GET "/" [] loading-page)
  (GET "/about" [] loading-page)
  (GET "/art" [] loading-page)

  (POST "/art" [] (fn [req] {:status 200 :body {:foo (str req)}}))
  (GET "/art/pieces" [] (all-pieces))

  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))



;; (defroutes routes
;;   (GET "/" [] loading-page)
;;   (GET "/about" [] loading-page)
;;   (GET "/art" [] loading-page)

;;   (POST "/art" [] (fn [req] {:status 200 :body {:foo (str req)}}))
;;   (GET "/art/pieces" [] (all-pieces))

;;   (resources "/")
;;   (not-found "Not Found"))

;; ;; (defroutes api-routes
;; ;;   (POST "/art" [] (fn [req] {:status 200 :body {:foo (str req)}}))
;; ;;   (GET "/art/pieces" [] (all-pieces)))

;; ;; (def app (wrap-middleware #'routes))
;; (def app (wrap-api-middleware #'routes))
