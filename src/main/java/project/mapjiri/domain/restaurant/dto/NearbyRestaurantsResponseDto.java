package project.mapjiri.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;
import project.mapjiri.global.client.dto.KakaoNearbyRestaurantResponseDto;


import java.util.List;
import java.util.stream.Collectors;

import static project.mapjiri.global.client.dto.KakaoNearbyRestaurantResponseDto.*;

@Getter
public class NearbyRestaurantsResponseDto {
    private final int totalCount;
    private final List<RestaurantList> restaurantLists;

    @Builder
    private NearbyRestaurantsResponseDto(int totalCount, List<RestaurantList> restaurantLists) {
        this.totalCount = totalCount;
        this.restaurantLists = restaurantLists;
    }

    public static NearbyRestaurantsResponseDto from(List<KakaoNearbyRestaurantResponseDto.Documents> restaurantLists) {
        return NearbyRestaurantsResponseDto.builder()
                .totalCount(restaurantLists.size())
                .restaurantLists(
                        restaurantLists.stream()
                                .map(RestaurantList::new)
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Getter
    public static class RestaurantList {
        private final String restaurantName;
        private final String address;
        private final String roadAddressName;
        private final Double longitude;
        private final Double latitude;

        public RestaurantList(Documents documents) {
            this.restaurantName = documents.getRestaurantName();
            this.address = documents.getAddress();
            this.roadAddressName = documents.getRoadAddressName();
            this.longitude = documents.getLongitude();
            this.latitude = documents.getLatitude();
        }
    }
}
