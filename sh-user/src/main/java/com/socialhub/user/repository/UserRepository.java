
package com.socialhub.user.repository;

import com.socialhub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user
     * @return Optional containing the User if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return Optional containing the User if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their authService user ID.
     *
     * @param authUserId the user ID from AUTH_SERVICE
     * @return Optional containing the User if found
     */
    Optional<User> findByAuthUserId(Long authUserId);
}
