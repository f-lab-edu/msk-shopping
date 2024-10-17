CREATE TABLE USERS (
                       user_id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- Corresponds to userId (Primary Key)
                       password VARCHAR(255) NOT NULL,  -- Corresponds to password
                       email VARCHAR(255) NOT NULL UNIQUE,  -- Corresponds to email, assuming unique constraint
                       nick_name VARCHAR(100),  -- Corresponds to nickName
                       is_admin BOOLEAN  -- Corresponds to isAdmin
);


CREATE TABLE ACCESS_TOKEN (
                       token_id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- Corresponds to userId (Primary Key)
                       user_id BIGINT NOT NULL,
                       token_value VARCHAR(255) NOT NULL,
                       expiration TIMESTAMP NOT NULL
);

CREATE TABLE REFRESH_TOKEN (
                              token_id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- Corresponds to userId (Primary Key)
                              user_id BIGINT NOT NULL,
                              token_value VARCHAR(255) NOT NULL,
                              expiration TIMESTAMP NOT NULL
);
