CREATE TABLE levels (
  id VARCHAR(36),
  rank INTEGER UNIQUE,
  required_experience INTEGER UNIQUE,
  description TEXT,
  PRIMARY KEY (id)
);