package project.mapjiri.domain.restaurant.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.restaurant.model.Restaurant;

import java.util.List;

@Getter
@NoArgsConstructor
public class RestaurantListCreateResponseDto {
    private List<RestaurantCreateResponseDto> list;

    public static RestaurantListCreateResponseDto from(List<RestaurantCreateResponseDto> list) {
        return new RestaurantListCreateResponseDto(list);
    }

    private RestaurantListCreateResponseDto(List<RestaurantCreateResponseDto> list) {
        this.list = list;
    }
}
