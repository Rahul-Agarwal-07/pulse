package com.rahul.pulse.post.infrastructure;

import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.posts.domain.model.FeedPostView;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.repository.FeedRepository;
import com.rahul.pulse.posts.domain.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@Transactional
public class FeedRepositoryAdapterIT {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("pulse_test")
                    .withUsername("postgres")
                    .withPassword("postgres");


    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_get_feed_successfully()
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
                "caption1",
                "imageUrl1"
        );

        Post post2 = Post.create(
                user.getId(),
                "caption2",
                "imageUrl2"
        );

        postRepository.save(post1);
        postRepository.save(post2);

        List<FeedPostView> feed = feedRepository.getFeed(
                user.getId(),
                0,
                10
        );

        assertNotNull(feed);
        assertEquals(2, feed.size());
    }
}
