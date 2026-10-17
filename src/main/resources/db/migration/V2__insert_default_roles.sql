INSERT INTO roles (id, name, description, created_at)
VALUES
    (gen_random_uuid(), 'ADMIN', 'System administrator', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'MANAGER', 'Business manager', CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'STAFF', 'Business staff member', CURRENT_TIMESTAMP);