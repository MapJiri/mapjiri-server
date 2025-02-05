package project.mapjiri.domain.menustar.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.user.model.User;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class MenuStar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuStarId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

}
