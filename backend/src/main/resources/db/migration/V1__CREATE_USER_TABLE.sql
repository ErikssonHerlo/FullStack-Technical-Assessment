-- Create the enum type
CREATE TYPE user_role AS ENUM ('ADMIN', 'PUBLISHER');

-- Note: the use of plural form "users" is intentional to follow naming conventions
CREATE TABLE users (
                       email VARCHAR(255) PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role user_role NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP NULL
);

-- Index for faster queries by role
CREATE INDEX idx_user_role ON users(role);
