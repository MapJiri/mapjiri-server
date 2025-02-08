package project.mapjiri.global.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import project.mapjiri.domain.place.dto.AddressResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


class KakaoMapClientTest {

    private MockRestServiceServer mockRestServiceServer;
    private KakaoMapClient kakaoMapClient;

    private final String KAKAO_BASE_URL = "https://dapi.kakao.com/v2/local";
    private final Double TEST_LONGITUDE = 127.3346;
    private final Double TEST_LATITUDE = 36.3588;

    @BeforeEach
    void setUp() {
        RestClient.Builder testBuilder = RestClient.builder().baseUrl(KAKAO_BASE_URL);
        mockRestServiceServer = MockRestServiceServer.bindTo(testBuilder).build();
        kakaoMapClient = new KakaoMapClient(testBuilder.build());
    }

    @Test
    void 좌표를_주소로_변환한다() {
        //given
        String kakaoResponse = """
                {
                    "meta": {
                        "total_count": 2
                    },
                    "documents": [
                        {
                            "region_type": "B",
                            "code": "3020011700",
                            "address_name": "대전광역시 유성구 장대동",
                            "region_1depth_name": "대전광역시",
                            "region_2depth_name": "유성구",
                            "region_3depth_name": "장대동",
                            "region_4depth_name": "",
                            "x": 127.33212878811635,
                            "y": 36.36239066685866
                        },
                        {
                            "region_type": "H",
                            "code": "3020054000",
                            "address_name": "대전광역시 유성구 온천2동",
                            "region_1depth_name": "대전광역시",
                            "region_2depth_name": "유성구",
                            "region_3depth_name": "온천2동",
                            "region_4depth_name": "",
                            "x": 127.33338051944209,
                            "y": 36.365375474818876
                        }
                    ]
                }
                """;

        mockRestServiceServer.expect(requestTo(KAKAO_BASE_URL + "/geo/coord2regioncode.json?x=" + TEST_LONGITUDE + "&y=" + TEST_LATITUDE))
                .andExpect(queryParam("x", TEST_LONGITUDE.toString()))
                .andExpect(queryParam("y", TEST_LATITUDE.toString()))
                .andRespond(withSuccess(kakaoResponse, MediaType.APPLICATION_JSON));

        //when
        AddressResponseDto addressResponseDto = kakaoMapClient.getAddressFromCoordinate(TEST_LONGITUDE, TEST_LATITUDE);

        //then
        assertAll(
                () -> assertNotNull(addressResponseDto),
                () -> assertThat(addressResponseDto.getAddress()).isEqualTo("대전광역시 유성구 온천2동"),
                () -> assertThat(addressResponseDto.getGu()).isEqualTo("유성구"),
                () -> assertThat(addressResponseDto.getDong()).isEqualTo("온천2동"),
                () -> assertThat(addressResponseDto.getLongitude()).isEqualTo(127.33338051944209),
                () -> assertThat(addressResponseDto.getLatitude()).isEqualTo(36.365375474818876)
        );
        mockRestServiceServer.verify();
    }
}