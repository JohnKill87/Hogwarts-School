-- liquibase formatted sql

-- changeset john:1
CREATE INDEX student_name_index ON student (name);

-- changeset john:2

CREATE INDEX faculty_nc_idx ON faculty(name, color);