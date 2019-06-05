(ns my-pharmacy.test.db.core
  (:require
    [my-pharmacy.db.core :refer [*db*] :as db]
    [luminus-migrations.core :as migrations]
    [clojure.test :refer :all]
    [clojure.java.jdbc :as jdbc]
    [java-time :as t]
    [my-pharmacy.config :refer [env]]
    [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'my-pharmacy.config/env
      #'my-pharmacy.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-medications
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (let [created_at (java.time.LocalDateTime/now)]
      (is (= 1 (db/create-medication!
                t-conn
                {:id         1
                 :medication_name "Aspirin"
                 :generic_name  "Aspirin"
                 :uses      "Aspirin is used to reduce fever and relieve mild to moderate pain from conditions such as muscle aches, toothaches, common cold, and headaches."
                 :side_effects       "Upset stomach and heartburn may occur. If either of these effects persist or worsen, tell your doctor or pharmacist promptly."
                 :precautions "Before taking aspirin, tell your doctor or pharmacist if you are allergic to it; or to other salicylates (such as choline salicylate); or to other pain relievers or fever reducers (other NSAIDs such as ibuprofen, naproxen); or if you have any other allergies."
                 :stock 2,
                 :expiration_date "2021-03-23",
                 :created_at created_at}
                {:connection t-conn})))
      (is (= {:id         1
                 :medication_name "Aspirin"
                 :generic_name  "Aspirin"
                 :uses      "Aspirin is used to reduce fever and relieve mild to moderate pain from conditions such as muscle aches, toothaches, common cold, and headaches."
                 :side_effects       "Upset stomach and heartburn may occur. If either of these effects persist or worsen, tell your doctor or pharmacist promptly."
                 :precautions "Before taking aspirin, tell your doctor or pharmacist if you are allergic to it; or to other salicylates (such as choline salicylate); or to other pain relievers or fever reducers (other NSAIDs such as ibuprofen, naproxen); or if you have any other allergies."
              :stock 2
              :expiration_date (t/local-date 2021 03 23),
                 :created_at created_at}
             (-> (db/get-medications t-conn {})
     
                 (select-keys [:id :medication_name :generic_name :uses :side_effects :precautions :stock :expiration_date :created_at])))))))
