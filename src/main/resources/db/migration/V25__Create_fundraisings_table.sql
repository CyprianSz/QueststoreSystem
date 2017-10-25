CREATE TABLE fundraisings (
  id VARCHAR(36),
  name TEXT UNIQUE,
  creation_date DATE,
  is_open BOOLEAN DEFAULT 1,
  PRIMARY KEY (id)
);