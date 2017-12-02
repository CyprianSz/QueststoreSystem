CREATE TABLE sessions (
  id VARCHAR(36) UNIQUE,
  user_id VARCHAR(36) ,
  user_first_name VARCHAR(40),
  user_last_name VARCHAR(255),
  user_email VARCHAR(255),
  user_type VARCHAR(20),
  PRIMARY KEY (id)
);
