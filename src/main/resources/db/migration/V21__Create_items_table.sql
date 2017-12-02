CREATE TABLE items (
  id VARCHAR(36),
  artifact_id VARCHAR(36) NOT NULL ,
  wallet_id VARCHAR(36) NOT NULL ,
  creation_date DATE NOT NULL,
  is_spent BOOLEAN DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (artifact_id) REFERENCES artifacts(id),
  FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);