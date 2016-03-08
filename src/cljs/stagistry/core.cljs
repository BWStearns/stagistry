(ns stagistry.core
    (:require [reagent.core :as reagent :refer [atom]]
              [ajax.core :refer [POST GET]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

;; -------------------------
;; State Management

(def app-state (atom
  {:doc {}
    :saved? false
    :page-state {}
    :all-pieces [
      {:title "Silence of the Lambda"
       :desc "I ate his clojure with some java beans and a nice chianti"
       :price 5000}
      {:title "Lambda and Goliath"
       :desc "Details the epic battle between the lambda and the giant Orange"
       :price nil}]}))

(defn set-value! [id value]
  (swap! app-state assoc :saved? false)
  (swap! app-state assoc-in [:doc id] value))

(defn get-value [id]
  (get-in @app-state [:doc id]))

(defn add-piece [piece]
  (POST "/art" :params piece)
  (swap! app-state update-in [:all-pieces] #(into [] (cons %2 %1)) piece))

;; -------------------------
;; Components

(defn form-input [tag inp-type id label]
  [tag label
   [:input
     {:type inp-type
       :class "form-control"
       :value (get-value id)
       :on-change #(set-value! id (-> % .-target .-value))}]])

(defn text-input [id label]
  (form-input :row "text" id label))

(defn num-input [id label]
  (form-input :row "number" id label))

(defn art-piece [piece]
  [:div.art-piece
   [:img.art-image {:src "http://media.moddb.com/cache/images/mods/1/15/14206/thumb_620x2000/oplamlogo.png"}]
   [:div.art-title (:title piece)]
   [:div.art-desc (:desc piece)]
   [:div.art-price (or (:price piece) "Unk")]
  ])

(defn pieces-component []
  [:ul (for [piece (:all-pieces @app-state)]
         [:li (art-piece piece)])])

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
  [:div
   [:div [:a {:href "/"} "go to the home page"]]
   [:div [:a {:href "/about"} "go to about page"]]
   [:div [:h2 "Stagistry Art!"]]
   [:div [:form
     [:p (text-input :title "Title")]
     [:p (text-input :desc "Description")]
     [:p (num-input :price "Price")]
     [:input {:type "button"
               :class "btn btn-default"
               :onClick #(add-piece (:doc @app-state))
               :value "Add Piece"}]]]
   [:div (pieces-component)]])

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


