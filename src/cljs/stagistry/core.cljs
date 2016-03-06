(ns stagistry.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

;; -------------------------
;; State

(def app-state (atom {:doc {} :saved? false}))

(defn set-value! [id value]
  (swap! app-state assoc :saved? false)
  (swap! app-state assoc-in [:doc id] value))

(defn get-value [id]
  (get-in @app-state [:doc id]))


;; -------------------------
;; Components

(defn text-input [id label]
  [:row label
   [:input
     {:type "text"
       :class "form-control"
       :value (get-value id)
       :on-change #(set-value! id (-> % .-target .-value))}]])

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to stagistry"]
   [:div [:a {:href "/about"} "go to about page"]]
   [:div [:a {:href "/art"} "go to art page"]]])

(defn about-page []
  [:div [:h2 "About stagistry"]
   [:div [:a {:href "/"} "go to the home page"]]
   [:div [:a {:href "/art"} "go to art page"]]])

(defn art-index-page []
    [:div [:h2 "Stagistry Art!"]]
    [:div [:form
     [:p (text-input :piece-name "Piece Name")]
     [:p (get-value :piece-name)]
     [:p "Description"] [:textarea {:type "text"}]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

(secretary/defroute "/art" []
  (session/put! :current-page #'art-index-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))

(def state (atom {}))


