CREATE TABLE mentors_groups (
  group_id VARCHAR(36),
  mentor_id VARCHAR(36),
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (mentor_id) REFERENCES mentors(id)
);