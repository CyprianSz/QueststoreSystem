CREATE TABLE achievements (
  id VARCHAR(36) NOT NULL ,
  quest_id VARCHAR(36) NOT NULL,
  codecooler_id VARCHAR(36) NOT NULL,
  creation_date DATE NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (quest_id) REFERENCES quests(id),
  FOREIGN KEY (codecooler_id) REFERENCES codecoolers(id)
);