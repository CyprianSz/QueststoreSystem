CREATE TABLE teams (
  id VARCHAR(36),
  group_id VARCHAR(36),
  name TEXT,
  PRIMARY KEY (id),
  FOREIGN KEY (group_id) REFERENCES groups(id)
);