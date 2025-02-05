package project.mapjiri.domain.place.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.placeStar.model.PlaceStar;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    private String gu;
    private String dong;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeStarId")
    private PlaceStar restaurants;
}
