(ns my-pharmacy.routes.home
  (:require
    [my-pharmacy.layout :as layout]
    [my-pharmacy.db.core :as db]
    [clojure.java.io :as io]
    [my-pharmacy.middleware :as middleware]
    [ring.util.http-response :as response]
    [struct.core :as st]))

(def medication-schema
  [[:medication_name
    st/required
    st/string
    {:message "Please enter a name for the medication"}]

   [:generic_name
    st/string]

   [:uses
    st/string
    st/required]

   [:side_effects
    st/string]

   [:precautions
    st/string]

   [:stock
    st/required
    {:message "Please enter a quantity"}]

   [:expiration_date
    st/required
    st/string]])

(defn validate-medication [params]
  (first (st/validate params medication-schema)))

(defn create-medication! [{:keys [params]}]
  (if-let [errors (validate-medication params)]
    (-> (response/found "/")
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/create-medication!
       (assoc params :created_at (java.util.Date.)))
      (response/found "/"))))

(defn update-medication! [{:keys [params]}]
  (if-let [errors (validate-medication params)]
    (-> (response/found (str "/edit?id=" (:id params)))
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/update-medication!
       (assoc params :created_at (java.util.Date.)))
      (response/found "/"))))

(defn delete-medication! [{:keys [query-params]}]
  (do
    (pr query-params)
    (db/delete-medication! (assoc {} :id (get query-params "id")))
    (response/found "/")))

(defn home-page [{:keys [flash] :as request}]
  (layout/render request "home.html" (merge {:medications (db/get-medications)}
                                            (select-keys flash [:name :message :errors]))))

(defn about-page [request]
  (layout/render request "about.html"))

(defn edit-page [{:keys [query-params flash] :as request}]
  (layout/render request "edit.html" (merge {:medication (db/get-medication (assoc {} :id (get query-params "id")))}
                                             (select-keys flash [:name :message :errors]))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page
         :post create-medication!}]
   ["/about" {:get about-page}]
   ["/edit" {:get edit-page
             :post update-medication!}]
   ["/delete" {:get delete-medication!}]])

