package project.mapjiri.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mapjiri.domain.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
}
