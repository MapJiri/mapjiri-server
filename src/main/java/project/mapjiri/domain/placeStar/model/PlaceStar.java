package project.mapjiri.domain.placeStar.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @JoinColumn(name = "user_id")
    private User user;
}
