-- ==========================================
-- Create table 'tasks'
-- ==========================================

CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       status VARCHAR(50) NOT NULL,
                       priority VARCHAR(50) NOT NULL,
                       due_date TIMESTAMP NOT NULL,
                       assigned_to VARCHAR(255), -- FK to users.email
                       created_by VARCHAR(255) NOT NULL, -- FK to users.email
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP NULL,

                       CONSTRAINT fk_assigned_to FOREIGN KEY (assigned_to) REFERENCES users(email),
                       CONSTRAINT fk_created_by FOREIGN KEY (created_by) REFERENCES users(email)
);

-- Indexes for performance on filtering
CREATE INDEX idx_task_status ON tasks(status);
CREATE INDEX idx_task_priority ON tasks(priority);
CREATE INDEX idx_task_due_date ON tasks(due_date);
