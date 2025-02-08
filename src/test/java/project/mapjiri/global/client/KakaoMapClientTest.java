package project.mapjiri.global.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import project.mapjiri.global.client.dto.KakaoAddressResponseDto;
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
        String kakaoResponse = "{\n" +
                "    \"meta\": {\n" +
                "        \"total_count\": 2\n" +
                "    },\n" +
                "    \"documents\": [\n" +
                "        {\n" +
                "            \"region_type\": \"B\",\n" +
                "            \"code\": \"3020011700\",\n" +
                "            \"address_name\": \"대전광역시 유성구 장대동\",\n" +
                "            \"region_1depth_name\": \"대전광역시\",\n" +
                "            \"region_2depth_name\": \"유성구\",\n" +
                "            \"region_3depth_name\": \"장대동\",\n" +
                "            \"region_4depth_name\": \"\",\n" +
                "            \"x\": 127.33212878811635,\n" +
                "            \"y\": 36.36239066685866\n" +
                "        },\n" +
                "        {\n" +
                "            \"region_type\": \"H\",\n" +
                "            \"code\": \"3020054000\",\n" +
                "            \"address_name\": \"대전광역시 유성구 온천2동\",\n" +
                "            \"region_1depth_name\": \"대전광역시\",\n" +
                "            \"region_2depth_name\": \"유성구\",\n" +
                "            \"region_3depth_name\": \"온천2동\",\n" +
                "            \"region_4depth_name\": \"\",\n" +
                "            \"x\": 127.33338051944209,\n" +
                "            \"y\": 36.365375474818876\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        mockRestServiceServer.expect(requestTo(KAKAO_BASE_URL + "/geo/coord2regioncode.json?x=" + TEST_LONGITUDE + "&y=" + TEST_LATITUDE))
                .andExpect(queryParam("x", TEST_LONGITUDE.toString()))
                .andExpect(queryParam("y", TEST_LATITUDE.toString()))
                .andRespond(withSuccess(kakaoResponse, MediaType.APPLICATION_JSON));

        //when
        KakaoAddressResponseDto responseDto = kakaoMapClient.getAddressFromCoordinate(TEST_LONGITUDE, TEST_LATITUDE);

        //then
        assertAll(
                () -> assertNotNull(responseDto),
                () -> assertFalse(responseDto.getDocuments().isEmpty()),
                () -> assertThat(responseDto.getDocuments().get(1).getRegionType()).isEqualTo("H"),
                () -> assertThat(responseDto.getDocuments().get(1).getAddressName()).isEqualTo("대전광역시 유성구 온천2동"),
                () -> assertThat(responseDto.getDocuments().get(1).getGu()).isEqualTo("유성구"),
                () -> assertThat(responseDto.getDocuments().get(1).getDong()).isEqualTo("온천2동"),
                () -> assertThat(responseDto.getDocuments().get(1).getLongitude()).isEqualTo(127.33338051944209),
                () -> assertThat(responseDto.getDocuments().get(1).getLatitude()).isEqualTo(36.365375474818876)
        );
        mockRestServiceServer.verify();
    }
}