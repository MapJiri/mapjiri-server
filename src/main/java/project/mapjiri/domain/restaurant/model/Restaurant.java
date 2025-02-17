package project.mapjiri.domain.restaurant.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.review.model.ReviewTag;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Restaurant {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long restaurantId;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String restaurantOldPlace;

    @Column(nullable = false)
    private String restaurantNewPlace;

    @Column(nullable = false)
    private String restaurantNumber;

    @Column(nullable = false)
    private ReviewTag topReviewTag;

    @Column(nullable = false)
    private Double restaurantLongitude;

    @Column(nullable = false)
    private Double restaurantLatitude;

    public Restaurant(String restaurantName, String restaurantOldPlace, String restaurantNewPlace, String restaurantNumber, ReviewTag reviewTag, Double restaurantLongitude, Double restaurantLatitude) {
        this.restaurantName = restaurantName;
        this.restaurantOldPlace = restaurantOldPlace;
        this.restaurantNewPlace = restaurantNewPlace;
        this.restaurantNumber = restaurantNumber;
        this.topReviewTag = reviewTag;
        this.restaurantLongitude = restaurantLongitude;
        this.restaurantLatitude = restaurantLatitude;
    }
}
