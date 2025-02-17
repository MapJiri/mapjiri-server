package project.mapjiri.domain.restaurant.service;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantCreateResponseDto {
    private Long id;

    public static RestaurantCreateResponseDto of(Long id) {
        return new RestaurantCreateResponseDto(id);
    }

    private RestaurantCreateResponseDto(Long id) {
        this.id = id;
    }
}
