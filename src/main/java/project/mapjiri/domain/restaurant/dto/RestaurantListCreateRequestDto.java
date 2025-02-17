package project.mapjiri.domain.restaurant.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RestaurantListCreateRequestDto {
    private List<RestaurantCreateRequestDto> list;
}
