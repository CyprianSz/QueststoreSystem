CREATE TABLE mentors (
  id VARCHAR(36),
  first_name VARCHAR(40),
  last_name VARCHAR(40),
  date_of_birth DATE,
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  PRIMARY KEY (id)
);