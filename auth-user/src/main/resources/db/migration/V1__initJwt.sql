CREATE TABLE REFRESH_TOKEN (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              token_value VARCHAR(255) NOT NULL,
                              user_id BIGINT NOT NULL UNIQUE,
                              expiration DATETIME NOT NULL
);

CREATE TABLE USERS (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Primary key with auto-increment
                       email VARCHAR(255) NOT NULL UNIQUE,       -- Email, unique and not null
                       password VARCHAR(255) NOT NULL,           -- Password, not null
                       is_admin BOOLEAN NOT NULL                  -- Boolean flag to indicate admin status
);

