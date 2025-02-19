package project.mapjiri.global.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoNearbyRestaurantResponseDto {

    @JsonProperty("meta")
    private Meta meta;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        @JsonProperty("is_end")
        private boolean inEnd;
    }

    @JsonProperty("documents")
    private List<Documents> documents;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    public static class Documents {

        @JsonProperty("place_name")
        private String restaurantName;

        @JsonProperty("address_name")
        private String address;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        @JsonProperty("phone")
        private String phoneNumber;

        @JsonProperty("x")
        private Double longitude;

        @JsonProperty("y")
        private Double latitude;
    }
}
