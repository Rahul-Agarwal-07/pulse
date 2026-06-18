CREATE TABLE posts(
    id UUID PRIMARY KEY,
    author_id UUID,
    caption TEXT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    likes_count INTEGER NOT NULL,
    comment_count INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT fk_posts_users FOREIGN KEY(author_id) REFERENCES users(id)
);