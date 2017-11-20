CREATE TABLE fundraisings (
  id VARCHAR(36) NOT NULL,
  name TEXT UNIQUE NOT NULL,
  CHECK (name !=""),
  creation_date DATE NOT NULL,
  is_open BOOLEAN DEFAULT 1,
  PRIMARY KEY (id)
);