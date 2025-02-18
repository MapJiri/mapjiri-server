package project.mapjiri.domain.restaurant.service;

import project.mapjiri.domain.restaurant.dto.RestaurantListCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateResponseDto;

public interface RestaurantService {
    RestaurantListCreateResponseDto registerRestaurantInfos(RestaurantListCreateRequestDto dto);
}
