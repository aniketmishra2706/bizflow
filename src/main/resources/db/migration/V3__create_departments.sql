CREATE TABLE departments (
                             id UUID PRIMARY KEY,
                             name VARCHAR(255) NOT NULL UNIQUE,
                             description VARCHAR(255),
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE users
    ADD COLUMN department_id UUID;

ALTER TABLE users
    ADD CONSTRAINT fk_users_department
        FOREIGN KEY (department_id)
            REFERENCES departments(id)
            ON DELETE SET NULL;

CREATE INDEX idx_users_department_id
    ON users(department_id);