CREATE TABLE artifacts (
  id VARCHAR(36) NOT NULL,
  name TEXT NOT NULL,
  CHECK (name !=""),
  description TEXT NOT NULL,
  CHECK (description !=""),
  type TEXT,
  value INTEGER DEFAULT 0,
  PRIMARY KEY (id)
);