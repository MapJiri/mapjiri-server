package project.mapjiri.domain.restaurant.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.restaurant.model.Restaurant;

import java.util.List;

@Getter
@NoArgsConstructor
public class RestaurantCreateResponseDto {
    private Long restaurantId;
    private List<Long> ReviewIds;

    public static RestaurantCreateResponseDto of(Long restaurantId, List<Long> reviewIds) {
        return new RestaurantCreateResponseDto(restaurantId, reviewIds);
    }

    public RestaurantCreateResponseDto(Long restaurantId, List<Long> reviewIds) {
        this.restaurantId = restaurantId;
        ReviewIds = reviewIds;
    }
}
