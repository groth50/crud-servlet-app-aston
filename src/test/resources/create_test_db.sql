CREATE TABLE roles
(
    role_id   IDENTITY NOT NULL,
    role_name TEXT,
    PRIMARY KEY (role_id)
);

CREATE TABLE users
(
    user_id  IDENTITY NOT NULL,
    user_name TEXT,
    role_id INTEGER,
    PRIMARY KEY (user_id),
    FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE
);
