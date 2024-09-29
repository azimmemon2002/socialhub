package com.socialhub.user.repository;

import com.socialhub.user.entity.Profile;
import com.socialhub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for Profile entity.
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /**
     * Finds a profile by the associated User.
     *
     * @param user the User entity
     * @return Optional containing the Profile if found
     */
    Optional<Profile> findByUser(User user);
}
