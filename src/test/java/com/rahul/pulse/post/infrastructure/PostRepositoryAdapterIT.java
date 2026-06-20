package com.rahul.pulse.post.infrastructure;

import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
public class PostRepositoryAdapterIT {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("pulse_test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_save_post()
    {
        Post post = Post.create(
                new UserId(UUID.randomUUID()),
                "this is a caption",
                "https://example.com"
        );

        postRepository.save(post);

        Post savedPost = postRepository.findById(post.getId())
                .orElse(null);

        assertNotNull(savedPost);
    }

    @Test
    void should_return_post_by_author_id()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        userRepository.save(user);

        Post post1 = Post.create(
                user.getId(),
                "this is post1 caption",
                "https://example.com"
        );

        Post post2 = Post.create(
                user.getId(),
                "this is post2 caption",
                "https://example.com"
        );

        postRepository.save(post1);
        postRepository.save(post2);

        List<Optional<Post>> posts = postRepository.findByAuthorId(user.getId());

        assertNotNull(posts);
        assertEquals(2, posts.size());
    }

    @Test
    void should_delete_post_by_id()
    {
        Post post = Post.create(
                new UserId(UUID.randomUUID()),
                "this is a caption",
                "https://example.com"
        );

        postRepository.save(post);

        postRepository.delete(post.getId());

        Post deletedPost = postRepository.findById(post.getId())
                .orElse(null);

        assertNull(deletedPost);
    }
}
