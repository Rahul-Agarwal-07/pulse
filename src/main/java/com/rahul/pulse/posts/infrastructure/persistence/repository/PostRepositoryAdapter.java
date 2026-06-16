package com.rahul.pulse.posts.infrastructure.persistence.repository;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.model.PostId;
import com.rahul.pulse.posts.domain.repository.PostRepository;
import com.rahul.pulse.posts.infrastructure.persistence.mapper.PostMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryAdapter implements PostRepository {

    final JpaPostRepository jpaPostRepository;

    public PostRepositoryAdapter(JpaPostRepository jpaPostRepository) {
        this.jpaPostRepository = jpaPostRepository;
    }

    @Override
    public Optional<Post> findById(PostId postId) {
        return jpaPostRepository.findById(postId.value())
                .map(PostMapper::toDomain);
    }

    @Override
    public List<Optional<Post>> findByAuthorId(UserId authorId) {
        return jpaPostRepository.findByAuthorId(authorId.value())
                .stream()
                .map(entity -> entity.map(PostMapper::toDomain))
                .toList();
    }

    @Override
    public void save(Post post) {
        jpaPostRepository.save(PostMapper.toEntity(post));
    }

    @Override
    public void delete(PostId postId) {
        jpaPostRepository.deleteById(postId.value());
    }
}
