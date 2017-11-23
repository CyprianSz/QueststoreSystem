CREATE TABLE levels (
  id VARCHAR(36) NOT NULL,
  rank INTEGER UNIQUE NOT NULL CHECK (rank >= 0),
  required_experience INTEGER UNIQUE NOT NULL CHECK (required_experience >= 0),
  description TEXT NOT NULL CHECK (description != ""),
  PRIMARY KEY (id)
);