DROP SCHEMA IF EXISTS ums;
CREATE SCHEMA ums;
USE ums;

CREATE TABLE ums.user (
    id BINARY(16) NOT NULL,
    username VARCHAR(45) NOT NULL UNIQUE,
    email VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);

CREATE TABLE ums.role (
    id BINARY(16) NOT NULL,
    name VARCHAR(45) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE ums.user_role (
    user_id BINARY(16) NOT NULL,
    role_id BINARY(16) NOT NULL,
    PRIMARY KEY(user_id, role_id),
    FOREIGN KEY(user_id) REFERENCES ums.user(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY(role_id) REFERENCES ums.role(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TRIGGER Add_Default_Role
    AFTER INSERT
    ON user FOR EACH ROW
    INSERT INTO user_role (user_id, role_id) VALUES (NEW.id, (SELECT id FROM role WHERE name = 'Subscriber'));