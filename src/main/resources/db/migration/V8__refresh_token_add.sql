CREATE TABLE refresh_token (
   user_id BIGINT PRIMARY KEY,
   token TEXT NOT NULL,
   created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
