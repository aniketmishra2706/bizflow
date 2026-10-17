CREATE TABLE workflows (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           description VARCHAR(1000),
                           status VARCHAR(255) NOT NULL,
                           department_id UUID,
                           created_by UUID,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_workflows_department
                               FOREIGN KEY (department_id)
                                   REFERENCES departments(id)
                                   ON DELETE SET NULL,

                           CONSTRAINT fk_workflows_created_by
                               FOREIGN KEY (created_by)
                                   REFERENCES users(id)
                                   ON DELETE SET NULL
);

CREATE INDEX idx_workflows_department_id
    ON workflows(department_id);

CREATE INDEX idx_workflows_created_by
    ON workflows(created_by);

CREATE INDEX idx_workflows_status
    ON workflows(status);