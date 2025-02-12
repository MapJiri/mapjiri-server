package project.mapjiri.domain.menustar.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.menu.model.Menu;
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private MenuStar(User user, Menu menu){
        this.user = user;
        this.menu = menu;
    }

    public static MenuStar of(User user, Menu menu){
        return new MenuStar(user, menu);
    }

}
