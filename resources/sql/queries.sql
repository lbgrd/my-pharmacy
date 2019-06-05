-- :name create-medication! :! :n
-- :doc creates a new medication record
INSERT INTO pharmacy
(medication_name, generic_name, uses, side_effects, precautions, stock, expiration_date, created_at)
VALUES (:medication_name, :generic_name, :uses, :side_effects, :precautions, :stock, :expiration_date, :created_at)

-- :name update-medication! :! :n
-- :doc updates an existing medication record
UPDATE pharmacy
SET medication_name = :medication_name, generic_name = :generic_name, uses = :uses, side_effects = :side_effects, precautions = :precautions, stock = :stock, expiration_date = :expiration_date
WHERE id = :id

-- :name get-medications :? :*
-- :doc retrieves medication records
SELECT * FROM pharmacy

-- :name get-medication :? :1
-- :doc retrieves medication record by id
SELECT * FROM pharmacy
WHERE id = :id

-- :name delete-medication! :! :n
-- :doc deletes a medication record given the id
DELETE FROM pharmacy
WHERE id = :id
