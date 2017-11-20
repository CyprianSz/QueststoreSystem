CREATE TABLE sessions (
  id VARCHAR(36) UNIQUE NOT NULL,
  user_id VARCHAR(36) NOT NULL,
  user_first_name VARCHAR(40) NOT NULL,
  CHECK (user_first_name !=""),
  user_last_name VARCHAR(255) NOT NULL,
  CHECK (user_last_name !=""),
  user_email VARCHAR(255) NOT NULL,
  CHECK (user_emil !=""),
  user_type VARCHAR(20) NOT NULL,
  CHECK (user_type !=""),
  PRIMARY KEY (id)
);
