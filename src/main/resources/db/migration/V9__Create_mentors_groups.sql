CREATE TABLE mentors_groups (
  group_id VARCHAR(36) NOT NULL,
  mentor_id VARCHAR(36) NOT  NULL,
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (mentor_id) REFERENCES mentors(id)
);