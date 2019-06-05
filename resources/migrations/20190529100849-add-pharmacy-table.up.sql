CREATE TABLE pharmacy
(id INTEGER PRIMARY KEY AUTO_INCREMENT,
 medication_name VARCHAR(60),
 generic_name VARCHAR(60),
 uses VARCHAR(300),
 side_effects VARCHAR(300),
 precautions VARCHAR(300),
 stock INTEGER,
 expiration_date DATE,
 created_at TIMESTAMP);
