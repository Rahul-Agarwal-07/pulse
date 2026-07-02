CREATE TABLE post_likes (

    id UUID PRIMARY KEY,

    post_id UUID NOT NULL,

    user_id UUID NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_post_likes_post
        FOREIGN KEY (post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_post_likes_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_post_likes_post_user
        UNIQUE (post_id, user_id)

);

CREATE INDEX idx_post_likes_post
    ON post_likes(post_id);

CREATE INDEX idx_post_likes_user
    ON post_likes(user_id);