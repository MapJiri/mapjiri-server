package project.mapjiri.domain.restaurant.service;

import project.mapjiri.domain.restaurant.dto.RestaurantListCreateRequestDto;

public interface RestaurantService {
    RestaurantListCreateResponseDto registerRestaurantInfos(RestaurantListCreateRequestDto dto);
}
