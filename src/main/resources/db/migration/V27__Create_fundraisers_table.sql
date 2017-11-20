CREATE TABLE fundraisers (
  fundraiser_id VARCHAR(36),
  fundraising_id VARCHAR(36),
  FOREIGN KEY (fundraiser_id) REFERENCES codecooler(id),
  FOREIGN KEY (fundraising_id) REFERENCES fundraisings(id)
);