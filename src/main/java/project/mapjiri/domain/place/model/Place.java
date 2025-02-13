package project.mapjiri.domain.place.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.placeStar.model.PlaceStar;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceStar> placeStars = new ArrayList<>();

    @Builder
    public Place(String gu, String dong){
        this.gu = gu;
        this.dong = dong;
    }

}
