CREATE TABLE COUPON (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,        -- Primary key with auto-increment
                        user_id BIGINT NOT NULL,                      -- User ID (foreign key or association)
                        coupon_policy_id BIGINT NOT NULL,              -- Coupon Policy ID (foreign key or association)
                        issued_at DATETIME NOT NULL                   -- Issued date and time
);

CREATE TABLE COUPON_POLICY (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- Primary key with auto-increment
                              name VARCHAR(255) NOT NULL,                 -- Name of the coupon policy
                              total_quantity INT NOT NULL,                 -- Total quantity of coupons available
                              type VARCHAR(255) NOT NULL,                 -- Type of coupon policy
                              event_end_at DATETIME NOT NULL                -- Event end date and time
);

