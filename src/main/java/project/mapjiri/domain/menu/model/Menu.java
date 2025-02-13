package project.mapjiri.domain.menu.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.menustar.model.MenuStar;
import project.mapjiri.domain.placeStar.model.PlaceStar;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false)
    private String menuType;

    @Column(nullable = false)
    private String menuName;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuStar> menuStars = new ArrayList<>();

    @Builder
    public Menu(String menuType, String menuName){
        this.menuType = menuType;
        this.menuName = menuName;
    }
}
