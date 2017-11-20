CREATE TABLE groups (
  id VARCHAR(36) NOT NULL,
  name TEXT NOT NULL,
  CHECK (name !=""),
  PRIMARY KEY (id)
);