(ns stagistry.query
  (:require [stagistry.database]
            [korma.core :refer :all]))



(defdb db (postgres {:db "stagistry"
                     :user "postgres"}))

(use 'korma.db)
(require '[clojure.string :as str])

;; ENTITIES

(defentity art-pieces
  (table :art_pieces)
  (entity-fields
    :id
    :name
    :description
    :price
    :artists_id)
  (belongs-to artists))

(defentity artists
  (table :artists)
  (entity-fields
    :id
    :name))

;; QUERIES

(defn all-pieces []
  (select art-pieces
          (fields
            :id
            :name
            :description
            :price)))

(defn create-piece [piece]
  (insert art-pieces
          (values {
                    :id (:id piece)
                    :name (:name piece)
                    :description (:description piece)
                    :price (:price piece)})))

