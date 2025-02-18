package project.mapjiri.domain.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
