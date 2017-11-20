CREATE TABLE fundraisings (
  id VARCHAR(36),
  name VARCHAR(50) NOT NULL,
  creation_date DATE,
  creator_id VARCHAR(50),
  is_open BOOLEAN DEFAULT 1,
  PRIMARY KEY (id),
  FOREIGN KEY (creator_id) REFERENCES codecoolers(id)
);