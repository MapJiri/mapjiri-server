package project.mapjiri.domain.placeStar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mapjiri.domain.place.model.Place;
import project.mapjiri.domain.placeStar.model.PlaceStar;
import project.mapjiri.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface  PlaceStarRepository extends JpaRepository<PlaceStar, Long> {

    List<PlaceStar> findByUser(User user);
    boolean existsByUserAndPlace(User user, Place place);
    Optional<PlaceStar> findByUserAndPlace(User user, Place place);

}
