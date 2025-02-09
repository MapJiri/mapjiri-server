package project.mapjiri.global.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import project.mapjiri.domain.place.dto.AddressResponseDto;
import project.mapjiri.domain.restaurant.dto.NearbyRestaurantsResponseDto;
import project.mapjiri.global.client.dto.KakaoAddressResponseDto;
import project.mapjiri.global.client.dto.KakaoNearbyRestaurantResponseDto;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class KakaoMapClient {

    private final RestClient restClient;
    private static final int MAX_PAGE = 2; // 페이지 당 15개의 가게, 즉 최대 30개의 가까운 가게만 조회

    public KakaoMapClient(RestClient kakaoRestClient) {
        this.restClient = kakaoRestClient;
    }


    public AddressResponseDto getAddressFromCoordinate(Double longitude, Double latitude) {
        String uri = UriComponentsBuilder.fromPath("/v2/local/geo/coord2regioncode.json")
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .build()
                .toUriString();
        KakaoAddressResponseDto response = sendGetRequest(uri).body(KakaoAddressResponseDto.class);
        return AddressResponseDto.from(response);
    }


    public NearbyRestaurantsResponseDto findNearbyRestaurants(Double longitude, Double latitude) {
        List<KakaoNearbyRestaurantResponseDto.Documents> allRestaurants = new ArrayList<>();

        for (int page = 1; page <= MAX_PAGE; page++) {
            String uri = UriComponentsBuilder.fromPath("/v2/local/search/category.json")
                    .queryParam("category_group_code", "FD6")
                    .queryParam("radius", 200)
                    .queryParam("x", longitude)
                    .queryParam("y", latitude)
                    .queryParam("page", page)
                    .build()
                    .toUriString();
            KakaoNearbyRestaurantResponseDto response = sendGetRequest(uri).body(KakaoNearbyRestaurantResponseDto.class);
            if (response == null || response.getDocuments() == null) {
                break;
            }
            allRestaurants.addAll(response.getDocuments());
            if (response.getMeta().isInEnd()) {
                break;
            }
        }
        return NearbyRestaurantsResponseDto.from(allRestaurants);
    }


    public NearbyRestaurantsResponseDto findNearbyRestaurantsByKeyword(Double longitude, Double latitude, String keyword) {
        List<KakaoNearbyRestaurantResponseDto.Documents> allRestaurants = new ArrayList<>();
        for (int page = 1; page <= MAX_PAGE; page++) {
            String uri = UriComponentsBuilder.fromPath("/v2/local/search/keyword.json")
                    .queryParam("query", keyword)
                    .queryParam("category_group_code", "FD6")
                    .queryParam("radius", 400)
                    .queryParam("x", longitude)
                    .queryParam("y", latitude)
                    .queryParam("page", page)
                    .build()
                    .toUriString();
            KakaoNearbyRestaurantResponseDto response = sendGetRequest(uri).body(KakaoNearbyRestaurantResponseDto.class);
            if (response == null || response.getDocuments() == null) {
                break;
            }
            allRestaurants.addAll(response.getDocuments());
            if (response.getMeta().isInEnd()) {
                break;
            }
        }
        return NearbyRestaurantsResponseDto.from(allRestaurants);
    }


    private RestClient.ResponseSpec sendGetRequest(String uri) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.error("외부 서버(카카오) 잚못된 요청 값");
                    throw new MyException(MyErrorCode.INVALID_REQUEST);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    log.error("외부 서버(카카오) 오류 발생");
                    throw new MyException(MyErrorCode.BAD_GATEWAY);
                });
    }
}
