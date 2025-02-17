package project.mapjiri.domain.restaurant.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Embedded
    @Column(nullable = false)
    private Tag topTag;


    public Restaurant(String uniqueKey, Tag tag) {
        this.uniqueKey = uniqueKey;
        this.topTag = tag;
    }
}
