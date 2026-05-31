package com.rahul.pulse.auth.infrastructure;

import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
public class UserRepositoryAdapterIT {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("pulse_test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_save_user()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail(user.getEmail());

        assertTrue(exists);
    }

    @Test
    void should_find_user_by_email()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        userRepository.save(user);

        final Optional<User> storedUser = userRepository.findByEmail(
                new Email("example@domain.com")
        );

        assertTrue(storedUser.isPresent());

        assertEquals(
                user.getId(),
                storedUser.get().getId()
        );

        assertEquals(
                user.getEmail().value(),
                storedUser.get().getEmail().value()
        );

        assertEquals(
                user.getFirstName(),
                storedUser.get().getFirstName()
        );

        assertEquals(
                user.getLastName(),
                storedUser.get().getLastName()
        );

        assertEquals(
                user.getPasswordHash().value(),
                storedUser.get().getPasswordHash().value()
        );
    }

    @Test
    void should_find_user_by_id()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        userRepository.save(user);

        Optional<User> storedUser = userRepository.findById(user.getId());

        assertTrue(storedUser.isPresent());

        assertEquals(
                user.getId(),
                storedUser.get().getId()
        );

        assertEquals(
                user.getEmail().value(),
                storedUser.get().getEmail().value()
        );

        assertEquals(
                user.getFirstName(),
                storedUser.get().getFirstName()
        );

        assertEquals(
                user.getLastName(),
                storedUser.get().getLastName()
        );

        assertEquals(
                user.getPasswordHash().value(),
                storedUser.get().getPasswordHash().value()
        );
    }

    @Test
    void should_return_empty_if_user_not_found()
    {
        Optional<User> user = userRepository.findByEmail(
                new Email("example@domain.com")
        );

        assertTrue(user.isEmpty());
    }

    @Test
    void should_return_false_if_email_not_exists()
    {
        boolean exists = userRepository.existsByEmail(
                new Email("example@domain.com")
        );

        assertFalse(exists);
    }
}
