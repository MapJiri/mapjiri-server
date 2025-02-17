package project.mapjiri.domain.restaurant.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Tag {
    private String name;
    private int count;

    public static Tag of(String name, int count) {
        return new Tag(name, count);
    }

    private Tag(String name, int count) {
        this.name = name;
        this.count = count;
    }
}
