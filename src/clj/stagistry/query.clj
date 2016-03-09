(ns stagistry.query
  (:require [stagistry.database]
            [korma.core :refer :all]))



(defdb db (postgres {:db "stagistry"
                     :user "postgres"}))

(use 'korma.db)
(require '[clojure.string :as str])

;; NEXT: create queries!
