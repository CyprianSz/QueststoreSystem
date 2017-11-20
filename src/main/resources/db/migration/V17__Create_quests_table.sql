CREATE TABLE quests (
  id VARCHAR(36) NOT NULL,
  name TEXT NOT NULL,
  description TEXT NOT NULL,
  CHECK (description !=""),
  reward INTEGER NOT NULL,
  CHECK (reward > 0),
  PRIMARY KEY (id)
);