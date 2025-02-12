package project.mapjiri.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.mapjiri.domain.menu.model.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT distinct m.menuType FROM Menu m")
    List<String> findDistinctType();

    @Query("SELECT m.menuName FROM Menu m WHERE m.menuType :menuType")
    List<String> findNameByType(@Param("menuType") String menuType);

    boolean existsByMenuTypeAndMenuName(String menuType, String menuName);
}