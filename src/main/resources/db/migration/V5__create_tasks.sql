CREATE TABLE tasks (
                       id UUID PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(2000),
                       status VARCHAR(255) NOT NULL,
                       priority VARCHAR(255) NOT NULL,
                       workflow_id UUID NOT NULL,
                       assigned_to UUID,
                       created_by UUID NOT NULL,
                       due_date TIMESTAMP,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_tasks_workflow
                           FOREIGN KEY (workflow_id)
                               REFERENCES workflows(id)
                               ON DELETE CASCADE,

                       CONSTRAINT fk_tasks_assigned_to
                           FOREIGN KEY (assigned_to)
                               REFERENCES users(id)
                               ON DELETE SET NULL,

                       CONSTRAINT fk_tasks_created_by
                           FOREIGN KEY (created_by)
                               REFERENCES users(id)
                               ON DELETE RESTRICT
);

CREATE INDEX idx_tasks_workflow_id
    ON tasks(workflow_id);

CREATE INDEX idx_tasks_assigned_to
    ON tasks(assigned_to);

CREATE INDEX idx_tasks_created_by
    ON tasks(created_by);

CREATE INDEX idx_tasks_status
    ON tasks(status);

CREATE INDEX idx_tasks_priority
    ON tasks(priority);