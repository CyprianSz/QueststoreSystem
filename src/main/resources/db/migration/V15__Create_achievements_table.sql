CREATE TABLE achievements (
  id VARCHAR(36),
  quest_id VARCHAR(36),
  codecooler_id VARCHAR(36),
  creation_date DATE,
  PRIMARY KEY (id),
  FOREIGN KEY (quest_id) REFERENCES quests(id),
  FOREIGN KEY (codecooler_id) REFERENCES codecoolers(id)
);