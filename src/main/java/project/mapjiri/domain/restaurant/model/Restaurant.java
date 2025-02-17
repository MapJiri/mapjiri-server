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
    private String uniqueKey;

    @Column(nullable = false)
    private ReviewTag topReviewTag;


    public Restaurant(String uniqueKey, ReviewTag reviewTag) {
        this.uniqueKey = uniqueKey;
        this.topReviewTag = reviewTag;
    }
}
