package project.mapjiri.domain.placeInfo.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.restaurant.model.Restaurant;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PlaceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PlaceInfo;

    @Column(nullable = false)
    private String placeInfoName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
