package project.mapjiri.global.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class KakaoAddressResponseDto {

    @JsonProperty("documents")
    private List<Documents> documents;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    public static class Documents {

        @JsonProperty("region_type")
        private String regionType;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("region_2depth_name")
        private String gu;

        @JsonProperty("region_3depth_name")
        private String dong;

        @JsonProperty("x")
        private Double longitude;

        @JsonProperty("y")
        private Double latitude;
    }
}
