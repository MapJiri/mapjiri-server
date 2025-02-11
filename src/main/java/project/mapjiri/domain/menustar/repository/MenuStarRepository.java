package project.mapjiri.domain.menustar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mapjiri.domain.menu.model.Menu;
import project.mapjiri.domain.menustar.model.MenuStar;
import project.mapjiri.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface MenuStarRepository extends JpaRepository<MenuStar, Long> {

    boolean existsByUserAndMenu(User user, Menu menu);
    Optional<MenuStar> findByUserAndMenu(User user, Menu menu);
    List<MenuStar> findByUser(User user);

}
