package project.mapjiri.domain.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateResponseDto;
import project.mapjiri.domain.restaurant.service.RestaurantService;
import project.mapjiri.global.dto.ResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/info")
    public ResponseDto<RestaurantListCreateResponseDto> registerRestaurantInfo(@RequestBody RestaurantListCreateRequestDto requestBody) {

        return ResponseDto.of(restaurantService.registerRestaurantInfos(requestBody), "성공");
    }
}
