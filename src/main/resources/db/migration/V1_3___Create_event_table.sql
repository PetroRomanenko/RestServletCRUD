CREATE TABLE IF NOT EXISTS events
    (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL
        CONSTRAINT fk_user REFERENCES users (id) ON DELETE CASCADE,
    file_id INTEGER NOT NULL
        CONSTRAINT fk_file REFERENCES files (id) ON DELETE CASCADE
    );