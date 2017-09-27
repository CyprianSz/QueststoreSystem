CREATE TABLE codecoolers (
  id VARCHAR(36),
  first_name VARCHAR(40),
  last_name VARCHAR(40),
  date_of_birth DATE,
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  wallet_id VARCHAR(36),
  group_id VARCHAR(36),
  team_id VARCHAR(36),
  level_id VARCHAR(36),
  PRIMARY KEY (id),
  FOREIGN KEY (wallet_id) REFERENCES wallets(id),
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (team_id) REFERENCES teams(id),
  FOREIGN KEY (level_id) REFERENCES levels(id)
);