package project.mapjiri.domain.placeStar.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.place.model.Place;
import project.mapjiri.domain.user.model.User;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PlaceStar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeStarId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private PlaceStar(User user, Place place){
        this.user = user;
        this.place = place;
    }

    public static PlaceStar of(User user, Place place){
        return new PlaceStar(user, place);
    }

}
