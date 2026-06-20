package com.rahul.pulse.posts.domain.repository;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.model.PostId;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(PostId postId);
    List<Optional<Post>> findByAuthorId(UserId authorId);

    void save(Post post);
    void delete(PostId postId);

}
