CREATE TABLE items (
  id VARCHAR(36),
  artifact_id VARCHAR(36),
  wallet_id VARCHAR(36),
  creation_date DATE,
  is_spent BOOLEAN DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (artifact_id) REFERENCES artifacts(id),
  FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);