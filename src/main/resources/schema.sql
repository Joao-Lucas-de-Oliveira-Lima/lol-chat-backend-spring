CREATE TABLE IF NOT EXISTS champions (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    title TEXT NOT NULL,
    lore TEXT,
    image_url TEXT
);