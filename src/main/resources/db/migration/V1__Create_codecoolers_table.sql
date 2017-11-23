CREATE TABLE codecoolers (
  id VARCHAR(36) NOT NULL,
  first_name VARCHAR(40) NOT NULL CHECK (first_name !=""),
  last_name VARCHAR(40) NOT NULL CHECK (last_name !=""),
  date_of_birth DATE NOT NULL CHECK (date_of_birth !=""),
  email VARCHAR(255) UNIQUE NOT NULL CHECK (email !=""),
  password VARCHAR(255) NOT NULL CHECK (password !=""),
  wallet_id VARCHAR(36) NOT NULL,
  group_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36),
  level_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (wallet_id) REFERENCES wallets(id),
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (team_id) REFERENCES teams(id),
  FOREIGN KEY (level_id) REFERENCES levels(id)
);
