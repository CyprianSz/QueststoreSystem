CREATE TABLE artifacts (
  id VARCHAR(36),
  name TEXT,
  description TEXT,
  type TEXT,
  value INTEGER DEFAULT 0,
  PRIMARY KEY (id)
);