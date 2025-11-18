ALTER TABLE users
    ADD COLUMN IF NOT EXISTS password VARCHAR(255);

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS roles TEXT;

-- Jeśli chcesz, ustawić wartość domyślną dla istniejących użytkowników:
UPDATE users SET roles = 'USER' WHERE roles IS NULL;
