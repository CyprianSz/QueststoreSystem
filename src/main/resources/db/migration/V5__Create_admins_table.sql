CREATE TABLE admins (
  id VARCHAR(36) NOT NULL,
  first_name VARCHAR(40) NOT NULL,
  CHECK (first_name !=""),
  last_name VARCHAR(40) NOT NULL,
  CHECK (last_name !=""),
  date_of_birth DATE NOT NULL,
  CHECK (date_of_birth !=""),
  email VARCHAR(255) UNIQUE NOT NULL,
  CHECK (email !=""),
  password VARCHAR(255) NOT NULL,
  CHECK (password !=""),
  PRIMARY KEY (id)
);