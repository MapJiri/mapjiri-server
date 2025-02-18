package project.mapjiri.domain.review.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.mapjiri.domain.review.model.Review;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.restaurant.restaurantId = :restaurantId")
    Page<Review> findReviewsByRestaurantId(@Param(value = "restaurantId") Long restaurantId, Pageable pageable);

    @Query("SELECT AVG(r.reviewPoint) FROM Review r WHERE r.restaurant.restaurantId = :restaurantId")
    Optional<Double> findAverageReviewPointByRestaurantId(@Param(value = "restaurantId") Long restaurantId);


}
