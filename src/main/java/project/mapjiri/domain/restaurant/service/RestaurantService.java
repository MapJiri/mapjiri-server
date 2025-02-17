package project.mapjiri.domain.restaurant.service;

import project.mapjiri.domain.restaurant.dto.RestaurantCreateRequestDto;

public interface RestaurantService {
    RestaurantCreateResponseDto registerRestaurantInfo(RestaurantCreateRequestDto dto);
}
