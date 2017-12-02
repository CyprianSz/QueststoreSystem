CREATE TABLE wallets (
  id VARCHAR(36) NOT NULL,
  balance INTEGER DEFAULT 0,
  earned_coins INTEGER DEFAULT 0,
  PRIMARY KEY (id)
);