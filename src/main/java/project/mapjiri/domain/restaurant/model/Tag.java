package project.mapjiri.domain.restaurant.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Tag {
    private String topName;
    private int count;

    public static Tag of(String name, int count) {
        return new Tag(name, count);
    }

    private Tag(String topName, int count) {
        this.topName = topName;
        this.count = count;
    }
}
