package project.mapjiri.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.mapjiri.domain.place.model.Place;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select distinct p.gu from Place p")
    List<String> findDistinctGu();

    @Query("SELECT p.dong FROM Place p WHERE p.gu = :gu")
    List<String> findDongByGu(@Param("gu") String gu);

    boolean existsByGuAndDong(String gu, String dong);
}
