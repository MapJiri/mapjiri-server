package project.mapjiri.domain.restaurant.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.menu.model.Menu;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

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
