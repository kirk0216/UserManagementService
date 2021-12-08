DROP SCHEMA IF EXISTS ums;
CREATE SCHEMA ums;
USE ums;

CREATE TABLE ums.user (
    id BINARY(16) NOT NULL,
    username VARCHAR(45) NOT NULL,
    email VARCHAR(45),
    password VARCHAR(100),
    created TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);

CREATE TABLE ums.session (
    user_id BINARY(16) NOT NULL,
    token BINARY(16) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT NOW()
)

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

-- Create roles
INSERT INTO role (id, name) VALUES (X'90CECD2046744226A0CFFAA5BF32F6D1', 'Subscriber');
INSERT INTO role (id, name) VALUES (X'6EBC45228AC5401E94565D80D367A77A', 'Publisher');

-- Create users
-- Batman is a Producer
INSERT INTO user (id, username, email, password) VALUES (X'16601887A8FF48C78A16F0CB82765759', 'Batman', 'batman@wayneenterprises.com', 'b4tc45e');
INSERT INTO user_role (user_id, role_id) VALUES (X'16601887A8FF48C78A16F0CB82765759', X'6EBC45228AC5401E94565D80D367A77A');

-- Robin is a Subscriber
INSERT INTO user (id, username, email, password) VALUES (X'D3A9A38D0903448A92A706F00372544F', 'Robin', 'robin@wayneenterprises.com', 'b4tc45e');
INSERT INTO user_role (user_id, role_id) VALUES (X'D3A9A38D0903448A92A706F00372544F', X'90CECD2046744226A0CFFAA5BF32F6D1');

-- Joker is a Producer and Subscriber
INSERT INTO user (id, username, email, password) VALUES (X'E09917C95357494FAE17A1DAAEC79B3A', 'Joker', 'joker@injustice.org', 'h4h4h3h3');
INSERT INTO user_role (user_id, role_id) VALUES (X'E09917C95357494FAE17A1DAAEC79B3A', X'90CECD2046744226A0CFFAA5BF32F6D1');
INSERT INTO user_role (user_id, role_id) VALUES (X'E09917C95357494FAE17A1DAAEC79B3A', X'6EBC45228AC5401E94565D80D367A77A');

CREATE TRIGGER Add_Default_Role
    AFTER INSERT
    ON user FOR EACH ROW
    INSERT INTO user_role (user_id, role_id) VALUES (NEW.id, (SELECT id FROM role WHERE name = 'Subscriber'));