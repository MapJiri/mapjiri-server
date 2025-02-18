package project.mapjiri.domain.restaurant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mapjiri.domain.restaurant.dto.NearbyRestaurantsResponseDto;
import project.mapjiri.domain.restaurant.dto.SearchRankingResponseDto;
import project.mapjiri.domain.restaurant.service.SearchService;
import project.mapjiri.global.client.KakaoMapClient;
import project.mapjiri.global.dto.ResponseDto;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final KakaoMapClient kakaoMapClient;
    private final SearchService searchService;

    @GetMapping("/nearby")
    public ResponseEntity<ResponseDto<NearbyRestaurantsResponseDto>> searchNearbyRestaurants(@RequestParam double longitude, @RequestParam double latitude, @RequestParam(required = false) String keyword) {
        if (keyword == null) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(kakaoMapClient.findNearbyRestaurants(longitude, latitude), "근처 가게 조회 성공"));
        }
        searchService.incrementSearchCount(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(kakaoMapClient.findNearbyRestaurantsByKeyword(longitude, latitude, keyword), "근처 " + keyword + " 가게 조회 성공"));
    }

    @GetMapping("/rankings")
    public ResponseEntity<ResponseDto<List<SearchRankingResponseDto>>> searchRankings() {
         return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(searchService.getRankings(), "실시간 검색 키워드 랭킹 조회 성공"));
    }

    @PostMapping("/rankings/increment")
    public ResponseEntity<ResponseDto<Void>> incrementRankings(@RequestParam String keyword) {
        searchService.incrementSearchCount(keyword);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(null, "검색 횟수 반영 성공"));
    }
}
