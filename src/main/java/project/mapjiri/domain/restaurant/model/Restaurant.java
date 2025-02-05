package project.mapjiri.domain.restaurant.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.menu.model.Menu;
import project.mapjiri.domain.place.model.Place;


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
    private ReviewTag reviewTag;

    @Column(nullable = false)
    private Double restaurantLongitude;

    @Column(nullable = false)
    private Double restaurantLatitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;
}
