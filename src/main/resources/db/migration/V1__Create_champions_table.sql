CREATE TABLE IF NOT EXISTS champions (
    id BIGSERIAL PRIMARY KEY, -- ID auto-incrementado
    name VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    lore TEXT,
    image_url VARCHAR(255)
);