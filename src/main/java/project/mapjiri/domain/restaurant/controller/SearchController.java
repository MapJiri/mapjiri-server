package project.mapjiri.domain.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.mapjiri.domain.restaurant.dto.NearbyRestaurantsResponseDto;
import project.mapjiri.global.client.KakaoMapClient;
import project.mapjiri.global.dto.ResponseDto;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final KakaoMapClient kakaoMapClient;

    @GetMapping("/nearby")
    public ResponseEntity<ResponseDto<NearbyRestaurantsResponseDto>> searchNearbyRestaurants(@RequestParam double longitude, @RequestParam double latitude, @RequestParam(required = false) String keyword) {
        if (keyword == null) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(kakaoMapClient.findNearbyRestaurants(longitude, latitude), "근처 가게 조회 성공"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(kakaoMapClient.findNearbyRestaurantsByKeyword(longitude, latitude, keyword), "근처 " + keyword + " 가게 조회 성공"));
    }
}
