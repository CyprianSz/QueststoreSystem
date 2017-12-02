CREATE TABLE teams (
  id VARCHAR(36) NOT NULL ,
  group_id VARCHAR(36) NOT NULL,
  name TEXT NOT NULL CHECK (name != ""),
  PRIMARY KEY (id),
  FOREIGN KEY (group_id) REFERENCES groups(id)
);