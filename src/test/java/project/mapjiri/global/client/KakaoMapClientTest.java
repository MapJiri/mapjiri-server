package project.mapjiri.global.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import project.mapjiri.domain.place.dto.AddressResponseDto;
import project.mapjiri.domain.restaurant.dto.NearbyRestaurantsResponseDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class KakaoMapClientTest {

    private MockRestServiceServer mockRestServiceServer;
    private KakaoMapClient kakaoMapClient;

    private final String KAKAO_BASE_URI = "https://dapi.kakao.com";
    private final Double TEST_LONGITUDE = 127.3346;
    private final Double TEST_LATITUDE = 36.3588;

    @BeforeEach
    void setUp() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(KAKAO_BASE_URI);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        RestClient.Builder testBuilder = RestClient.builder().uriBuilderFactory(factory);
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

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(KAKAO_BASE_URI + "/v2/local/geo/coord2regioncode.json?x=" + TEST_LONGITUDE + "&y=" + TEST_LATITUDE))
                .andExpect(method(HttpMethod.GET))
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

    @Test
    void 근처_음식점들을_조회한다() {
        //given
        String firstKakaoResponse = """
                {
                    "documents": [
                        {
                            "address_name": "대전 유성구 장대동 280-10",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 육류,고기",
                            "distance": "139",
                            "id": "27312985",
                            "phone": "042-822-2667",
                            "place_name": "원조뒷고기",
                            "place_url": "http://place.map.kakao.com/27312985",
                            "road_address_name": "대전 유성구 장대로 41",
                            "x": "127.336150709515",
                            "y": "36.3586773933956"
                        },
                        {
                            "address_name": "대전 유성구 장대동 283-18",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 육류,고기 > 삼겹살",
                            "distance": "177",
                            "id": "9021814",
                            "phone": "042-822-2257",
                            "place_name": "만수불고기",
                            "place_url": "http://place.map.kakao.com/9021814",
                            "road_address_name": "대전 유성구 장대로 38",
                            "x": "127.336515015229",
                            "y": "36.3584132248304"
                        },
                        {
                            "address_name": "대전 유성구 장대동 281-10",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 찌개,전골",
                            "distance": "184",
                            "id": "10745840",
                            "phone": "042-823-1131",
                            "place_name": "원조촌돼지",
                            "place_url": "http://place.map.kakao.com/10745840",
                            "road_address_name": "대전 유성구 유성대로736번길 55",
                            "x": "127.336649938155",
                            "y": "36.3586976160728"
                        },
                        {
                            "address_name": "대전 유성구 장대동 191-4",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 순대",
                            "distance": "120",
                            "id": "18694538",
                            "phone": "042-822-2618",
                            "place_name": "부산식당",
                            "place_url": "http://place.map.kakao.com/18694538",
                            "road_address_name": "대전 유성구 유성대로730번길 34",
                            "x": "127.334417474799",
                            "y": "36.3577270112873"
                        },
                        {
                            "address_name": "대전 유성구 장대동 273-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 해장국",
                            "distance": "109",
                            "id": "1091458363",
                            "phone": "042-822-8815",
                            "place_name": "울엄마양평해장국 유성장대점",
                            "place_url": "http://place.map.kakao.com/1091458363",
                            "road_address_name": "대전 유성구 유성대로 756",
                            "x": "127.335067710601",
                            "y": "36.3597077747558"
                        },
                        {
                            "address_name": "대전 유성구 장대동 268-10",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 해물,생선 > 매운탕,해물탕",
                            "distance": "143",
                            "id": "1847693507",
                            "phone": "042-824-2303",
                            "place_name": "어부네해물탕조개찜",
                            "place_url": "http://place.map.kakao.com/1847693507",
                            "road_address_name": "대전 유성구 유성대로 737",
                            "x": "127.333041683622",
                            "y": "36.3590754072017"
                        },
                        {
                            "address_name": "대전 유성구 장대동 273-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 해물,생선",
                            "distance": "116",
                            "id": "437891450",
                            "phone": "042-826-2733",
                            "place_name": "명태마을",
                            "place_url": "http://place.map.kakao.com/437891450",
                            "road_address_name": "대전 유성구 유성대로 756",
                            "x": "127.335071346651",
                            "y": "36.3597762538509"
                        },
                        {
                            "address_name": "대전 유성구 장대동 282-16",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 국수 > 칼국수",
                            "distance": "77",
                            "id": "21226675",
                            "phone": "042-823-3393",
                            "place_name": "고향손칼국수",
                            "place_url": "http://place.map.kakao.com/21226675",
                            "road_address_name": "대전 유성구 유성대로752번길 33",
                            "x": "127.335298283699",
                            "y": "36.3583950171644"
                        },
                        {
                            "address_name": "대전 유성구 장대동 191-12",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 순대",
                            "distance": "167",
                            "id": "8051643",
                            "phone": "042-822-7040",
                            "place_name": "윤정식당",
                            "place_url": "http://place.map.kakao.com/8051643",
                            "road_address_name": "대전 유성구 유성대로720번길 35",
                            "x": "127.333890235924",
                            "y": "36.3574076673534"
                        },
                        {
                            "address_name": "대전 유성구 장대동 269-7",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 냉면",
                            "distance": "99",
                            "id": "9500141",
                            "phone": "042-822-4488",
                            "place_name": "청석골칡냉면",
                            "place_url": "http://place.map.kakao.com/9500141",
                            "road_address_name": "대전 유성구 유성대로 747",
                            "x": "127.33401752039862",
                            "y": "36.359557515415645"
                        },
                        {
                            "address_name": "대전 유성구 장대동 277-24",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식",
                            "distance": "93",
                            "id": "192559988",
                            "phone": "042-825-1894",
                            "place_name": "청천광장",
                            "place_url": "http://place.map.kakao.com/192559988",
                            "road_address_name": "대전 유성구 유성대로730번길 17-10",
                            "x": "127.33370713310654",
                            "y": "36.35836162223326"
                        },
                        {
                            "address_name": "대전 유성구 장대동 281-11",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 육류,고기 > 족발,보쌈",
                            "distance": "165",
                            "id": "27199584",
                            "phone": "042-825-4919",
                            "place_name": "장원족발",
                            "place_url": "http://place.map.kakao.com/27199584",
                            "road_address_name": "대전 유성구 장대로 42",
                            "x": "127.33644518037458",
                            "y": "36.35875496682332"
                        },
                        {
                            "address_name": "대전 유성구 장대동 280-10",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 육류,고기",
                            "distance": "134",
                            "id": "12366204",
                            "phone": "042-822-9224",
                            "place_name": "아저씨뒷고기 본점",
                            "place_url": "http://place.map.kakao.com/12366204",
                            "road_address_name": "대전 유성구 장대로 41",
                            "x": "127.3360894975836",
                            "y": "36.3586928855628"
                        },
                        {
                            "address_name": "대전 유성구 장대동 277-10",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식",
                            "distance": "87",
                            "id": "16686413",
                            "phone": "042-822-0657",
                            "place_name": "시장보리밥집",
                            "place_url": "http://place.map.kakao.com/16686413",
                            "road_address_name": "대전 유성구 유성대로730번길 33",
                            "x": "127.33447667417",
                            "y": "36.3580224310486"
                        },
                        {
                            "address_name": "대전 유성구 장대동 273-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 국수 > 칼국수",
                            "distance": "116",
                            "id": "262031843",
                            "phone": "042-380-8949",
                            "place_name": "명물옹심이메밀칼국수",
                            "place_url": "http://place.map.kakao.com/262031843",
                            "road_address_name": "대전 유성구 유성대로 756",
                            "x": "127.335098032348",
                            "y": "36.3597635625648"
                        }
                    ],
                    "meta": {
                        "is_end": false,
                        "pageable_count": 45,
                        "same_name": null,
                        "total_count": 81
                    }
                }
                """;

        String secondKakaoResponse = """
                {
                    "documents": [
                        {
                            "address_name": "대전 유성구 장대동 282-4",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 육류,고기 > 족발,보쌈",
                            "distance": "116",
                            "id": "12126994",
                            "phone": "042-826-7456",
                            "place_name": "유성길족발보쌈 본점",
                            "place_url": "http://place.map.kakao.com/12126994",
                            "road_address_name": "대전 유성구 유성대로736번길 40",
                            "x": "127.33584614206382",
                            "y": "36.3585050451441"
                        },
                        {
                            "address_name": "대전 유성구 장대동 280-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 해물,생선 > 회",
                            "distance": "125",
                            "id": "738548041",
                            "phone": "",
                            "place_name": "제일수산",
                            "place_url": "http://place.map.kakao.com/738548041",
                            "road_address_name": "대전 유성구 유성대로740번길 42",
                            "x": "127.335757295447",
                            "y": "36.3594282771178"
                        },
                        {
                            "address_name": "대전 유성구 장대동 283-14",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 국수 > 칼국수",
                            "distance": "187",
                            "id": "810523772",
                            "phone": "042-822-1005",
                            "place_name": "유성칼국수",
                            "place_url": "http://place.map.kakao.com/810523772",
                            "road_address_name": "대전 유성구 장대로 34",
                            "x": "127.336490186962",
                            "y": "36.3580807611501"
                        },
                        {
                            "address_name": "대전 유성구 장대동 280-14",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 국수 > 칼국수",
                            "distance": "111",
                            "id": "10016243",
                            "phone": "042-823-1350",
                            "place_name": "통나무식당",
                            "place_url": "http://place.map.kakao.com/10016243",
                            "road_address_name": "대전 유성구 유성대로758번길 27",
                            "x": "127.335822317254",
                            "y": "36.3590027398117"
                        },
                        {
                            "address_name": "대전 유성구 장대동 280-3",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 육류,고기",
                            "distance": "152",
                            "id": "39587254",
                            "phone": "042-822-2660",
                            "place_name": "농장부라더스 장대점",
                            "place_url": "http://place.map.kakao.com/39587254",
                            "road_address_name": "대전 유성구 장대로 51",
                            "x": "127.336038526758",
                            "y": "36.3595356275065"
                        },
                        {
                            "address_name": "대전 유성구 장대동 280-18",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 분식",
                            "distance": "130",
                            "id": "15386437",
                            "phone": "042-823-2949",
                            "place_name": "다락방",
                            "place_url": "http://place.map.kakao.com/15386437",
                            "road_address_name": "대전 유성구 장대로 43",
                            "x": "127.336053315883",
                            "y": "36.3588290647552"
                        },
                        {
                            "address_name": "대전 유성구 장대동 191-6",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 두부전문점",
                            "distance": "136",
                            "id": "1140641073",
                            "phone": "042-825-7782",
                            "place_name": "유성할매빈대떡",
                            "place_url": "http://place.map.kakao.com/1140641073",
                            "road_address_name": "대전 유성구 유성대로730번길 32",
                            "x": "127.33434111666392",
                            "y": "36.35758754278764"
                        },
                        {
                            "address_name": "대전 유성구 장대동 280-12",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 해물,생선 > 회",
                            "distance": "112",
                            "id": "11009670",
                            "phone": "042-825-2160",
                            "place_name": "대천수산",
                            "place_url": "http://place.map.kakao.com/11009670",
                            "road_address_name": "대전 유성구 유성대로758번길 31",
                            "x": "127.335857056185",
                            "y": "36.358789964872"
                        },
                        {
                            "address_name": "대전 유성구 장대동 277-23",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식",
                            "distance": "100",
                            "id": "1418036933",
                            "phone": "010-9723-1388",
                            "place_name": "명자네전집",
                            "place_url": "http://place.map.kakao.com/1418036933",
                            "road_address_name": "대전 유성구 유성대로730번길 17-8",
                            "x": "127.33371110493256",
                            "y": "36.35824806307253"
                        },
                        {
                            "address_name": "대전 유성구 장대동 282-7",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식",
                            "distance": "147",
                            "id": "135625230",
                            "phone": "042-822-3091",
                            "place_name": "궁민정음 유성장대점",
                            "place_url": "http://place.map.kakao.com/135625230",
                            "road_address_name": "대전 유성구 유성대로736번길 46",
                            "x": "127.33619669732133",
                            "y": "36.35848981960257"
                        },
                        {
                            "address_name": "대전 유성구 장대동 277-12",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 순대",
                            "distance": "95",
                            "id": "15756571",
                            "phone": "042-822-1245",
                            "place_name": "원조유성토종순대",
                            "place_url": "http://place.map.kakao.com/15756571",
                            "road_address_name": "대전 유성구 유성대로730번길 29",
                            "x": "127.334344952134",
                            "y": "36.35796332211598"
                        },
                        {
                            "address_name": "대전 유성구 장대동 269-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 국수 > 칼국수",
                            "distance": "155",
                            "id": "26424013",
                            "phone": "042-825-9944",
                            "place_name": "나인칼국수 유성점",
                            "place_url": "http://place.map.kakao.com/26424013",
                            "road_address_name": "대전 유성구 유성대로719번안길 54",
                            "x": "127.333941816405",
                            "y": "36.3600930249151"
                        },
                        {
                            "address_name": "대전 유성구 장대동 269-8",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 치킨 > 페리카나",
                            "distance": "98",
                            "id": "11250021",
                            "phone": "042-822-4342",
                            "place_name": "페리카나 유성2점",
                            "place_url": "http://place.map.kakao.com/11250021",
                            "road_address_name": "대전 유성구 유성대로 745",
                            "x": "127.333925936443",
                            "y": "36.3595055032538"
                        },
                        {
                            "address_name": "대전 유성구 장대동 278-2",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 술집 > 와인바",
                            "distance": "13",
                            "id": "21877840",
                            "phone": "042-822-8314",
                            "place_name": "트레비니",
                            "place_url": "http://place.map.kakao.com/21877840",
                            "road_address_name": "대전 유성구 유성대로736번길 19",
                            "x": "127.334733814314",
                            "y": "36.35874986045"
                        },
                        {
                            "address_name": "대전 유성구 장대동 279-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 해물,생선 > 추어",
                            "distance": "94",
                            "id": "12468965",
                            "phone": "042-477-3651",
                            "place_name": "한밭추어탕",
                            "place_url": "http://place.map.kakao.com/12468965",
                            "road_address_name": "대전 유성구 유성대로740번길 38",
                            "x": "127.33544260170268",
                            "y": "36.35931200826078"
                        }
                    ],
                    "meta": {
                        "is_end": false,
                        "pageable_count": 45,
                        "same_name": null,
                        "total_count": 81
                    }
                }
                """;

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(KAKAO_BASE_URI + "/v2/local/search/category.json?category_group_code=FD6&radius=200&x=" + TEST_LONGITUDE + "&y=" + TEST_LATITUDE + "&page=1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(firstKakaoResponse, MediaType.APPLICATION_JSON));

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(KAKAO_BASE_URI + "/v2/local/search/category.json?category_group_code=FD6&radius=200&x=" + TEST_LONGITUDE + "&y=" + TEST_LATITUDE + "&page=2"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(secondKakaoResponse, MediaType.APPLICATION_JSON));

        //when
        NearbyRestaurantsResponseDto nearbyRestaurantsResponseDto = kakaoMapClient.findNearbyRestaurants(TEST_LONGITUDE, TEST_LATITUDE);

        //then
        assertAll(
                () -> assertNotNull(nearbyRestaurantsResponseDto),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getRestaurantName()).isEqualTo("원조뒷고기"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getAddress()).isEqualTo("대전 유성구 장대동 280-10"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getRoadAddressName()).isEqualTo("대전 유성구 장대로 41"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getPhoneNumber()).isEqualTo("042-822-2667"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getLongitude()).isEqualTo(127.336150709515),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getLatitude()).isEqualTo( 36.3586773933956),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(29).getRestaurantName()).isEqualTo("한밭추어탕"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(29).getAddress()).isEqualTo("대전 유성구 장대동 279-1"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(29).getRoadAddressName()).isEqualTo("대전 유성구 유성대로740번길 38"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(29).getPhoneNumber()).isEqualTo("042-477-3651"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(29).getLongitude()).isEqualTo(127.33544260170268),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(29).getLatitude()).isEqualTo(36.35931200826078)
        );
        mockRestServiceServer.verify();
    }

    @Test
    void 근처_특정_음식점_리스트를_조회한다() {
        //given
        String keyword = URLEncoder.encode("돈까스", StandardCharsets.UTF_8);

        String firstKakaoResponse = """
                {
                    "documents": [
                        {
                            "address_name": "대전 유성구 장대동 281-25",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 일식 > 돈까스,우동",
                            "distance": "213",
                            "id": "39741484",
                            "phone": "010-2179-0986",
                            "place_name": "온전히소바",
                            "place_url": "http://place.map.kakao.com/39741484",
                            "road_address_name": "대전 유성구 문화원로6번길 48-1",
                            "x": "127.33664792211643",
                            "y": "36.35978173518269"
                        },
                        {
                            "address_name": "대전 유성구 장대동 356-6",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 일식 > 돈까스,우동",
                            "distance": "287",
                            "id": "1497475201",
                            "phone": "042-343-9998",
                            "place_name": "해오름돈까스",
                            "place_url": "http://place.map.kakao.com/1497475201",
                            "road_address_name": "대전 유성구 문화원로6번길 23",
                            "x": "127.336561159156",
                            "y": "36.3608498716973"
                        },
                        {
                            "address_name": "대전 유성구 장대동 271-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 일식 > 돈까스,우동",
                            "distance": "260",
                            "id": "1241412199",
                            "phone": "",
                            "place_name": "스가돈",
                            "place_url": "http://place.map.kakao.com/1241412199",
                            "road_address_name": "대전 유성구 장대로 69",
                            "x": "127.335211836787",
                            "y": "36.3610996855437"
                        },
                        {
                            "address_name": "대전 유성구 장대동 366-5",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 일식 > 돈까스,우동",
                            "distance": "381",
                            "id": "719564065",
                            "phone": "",
                            "place_name": "난바우동 대전유성점",
                            "place_url": "http://place.map.kakao.com/719564065",
                            "road_address_name": "대전 유성구 문화원로14번길 54",
                            "x": "127.33879212128939",
                            "y": "36.359390873029376"
                        },
                        {
                            "address_name": "대전 유성구 장대동 362-8",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 일식 > 돈까스,우동",
                            "distance": "379",
                            "id": "376503888",
                            "phone": "042-825-1146",
                            "place_name": "감성카츠 유성점",
                            "place_url": "http://place.map.kakao.com/376503888",
                            "road_address_name": "대전 유성구 문화원로14번길 53",
                            "x": "127.338728152631",
                            "y": "36.3595415505873"
                        },
                        {
                            "address_name": "대전 유성구 장대동 273-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 해물,생선",
                            "distance": "116",
                            "id": "437891450",
                            "phone": "042-826-2733",
                            "place_name": "명태마을",
                            "place_url": "http://place.map.kakao.com/437891450",
                            "road_address_name": "대전 유성구 유성대로 756",
                            "x": "127.335071346651",
                            "y": "36.3597762538509"
                        },
                        {
                            "address_name": "대전 유성구 구암동 527-57",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 국수",
                            "distance": "356",
                            "id": "802204708",
                            "phone": "042-823-8257",
                            "place_name": "시골막국수 유성점",
                            "place_url": "http://place.map.kakao.com/802204708",
                            "road_address_name": "대전 유성구 유성대로 711",
                            "x": "127.331128649965",
                            "y": "36.357229710135"
                        },
                        {
                            "address_name": "대전 유성구 장대동 280-18",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 분식",
                            "distance": "130",
                            "id": "15386437",
                            "phone": "042-823-2949",
                            "place_name": "다락방",
                            "place_url": "http://place.map.kakao.com/15386437",
                            "road_address_name": "대전 유성구 장대로 43",
                            "x": "127.336053315883",
                            "y": "36.3588290647552"
                        },
                        {
                            "address_name": "대전 유성구 장대동 360-14",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 양식",
                            "distance": "324",
                            "id": "223975565",
                            "phone": "042-823-5882",
                            "place_name": "용빠주방",
                            "place_url": "http://place.map.kakao.com/223975565",
                            "road_address_name": "대전 유성구 문화원로14번길 32",
                            "x": "127.337852524677",
                            "y": "36.3600820310509"
                        },
                        {
                            "address_name": "대전 유성구 장대동 353-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 분식 > 떡볶이 > 달토끼의떡볶이흡입구역",
                            "distance": "387",
                            "id": "1187614499",
                            "phone": "042-822-2855",
                            "place_name": "달토끼의떡볶이흡입구역 대전장대점",
                            "place_url": "http://place.map.kakao.com/1187614499",
                            "road_address_name": "대전 유성구 문화원로6번길 1",
                            "x": "127.33713053263",
                            "y": "36.361630486087"
                        },
                        {
                            "address_name": "대전 유성구 봉명동 561-8",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 도시락 > 본도시락",
                            "distance": "394",
                            "id": "18551854",
                            "phone": "042-823-4280",
                            "place_name": "본도시락 유성봉명점",
                            "place_url": "http://place.map.kakao.com/18551854",
                            "road_address_name": "대전 유성구 장대로 9",
                            "x": "127.33654192918821",
                            "y": "36.35560688926996"
                        },
                        {
                            "address_name": "대전 유성구 봉명동 561-11",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 도시락 > 한솥도시락",
                            "distance": "398",
                            "id": "2124342746",
                            "phone": "042-824-0200",
                            "place_name": "한솥도시락 대전유성봉명점",
                            "place_url": "http://place.map.kakao.com/2124342746",
                            "road_address_name": "대전 유성구 계룡로 45",
                            "x": "127.336132303562",
                            "y": "36.3554332142247"
                        },
                        {
                            "address_name": "대전 유성구 장대동 353-4",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 도시락 > 한솥도시락",
                            "distance": "392",
                            "id": "26569317",
                            "phone": "042-822-6089",
                            "place_name": "한솥도시락 대전장대점",
                            "place_url": "http://place.map.kakao.com/26569317",
                            "road_address_name": "대전 유성구 문화원로 12",
                            "x": "127.337530782637",
                            "y": "36.3614310976739"
                        },
                        {
                            "address_name": "대전 유성구 봉명동 562-12",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 분식 > 고봉민김밥인",
                            "distance": "346",
                            "id": "26840850",
                            "phone": "042-826-3452",
                            "place_name": "고봉민김밥인 대전유성터미널점",
                            "place_url": "http://place.map.kakao.com/26840850",
                            "road_address_name": "대전 유성구 계룡로 33-1",
                            "x": "127.33486327670654",
                            "y": "36.3556800947109"
                        },
                        {
                            "address_name": "대전 유성구 장대동 370-5",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 양식 > 피자 > 프레드피자",
                            "distance": "361",
                            "id": "1857172506",
                            "phone": "042-825-8806",
                            "place_name": "프레드피자 대전유성점",
                            "place_url": "http://place.map.kakao.com/1857172506",
                            "road_address_name": "대전 유성구 문화원로6번길 87-14",
                            "x": "127.338556199474",
                            "y": "36.3581695485215"
                        }
                    ],
                    "meta": {
                        "is_end": false,
                        "pageable_count": 17,
                        "same_name": {
                            "keyword": "돈까스",
                            "region": [],
                            "selected_region": ""
                        },
                        "total_count": 17
                    }
                }
                """;

        String secondKakaoResponse = """
                {
                    "documents": [
                        {
                            "address_name": "대전 유성구 봉명동 566-1",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 분식 > 떡볶이",
                            "distance": "397",
                            "id": "606568642",
                            "phone": "",
                            "place_name": "만능떡볶이",
                            "place_url": "http://place.map.kakao.com/606568642",
                            "road_address_name": "대전 유성구 계룡로 40",
                            "x": "127.335346143323",
                            "y": "36.3552669033184"
                        },
                        {
                            "address_name": "대전 유성구 장대동 270-5",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 한식 > 국밥 > 이것이국밥이다",
                            "distance": "204",
                            "id": "1389994811",
                            "phone": "",
                            "place_name": "이것이국밥이다 대전유성점",
                            "place_url": "http://place.map.kakao.com/1389994811",
                            "road_address_name": "대전 유성구 유성대로 757-39",
                            "x": "127.334242673825",
                            "y": "36.360621173302"
                        }
                    ],
                    "meta": {
                        "is_end": true,
                        "pageable_count": 17,
                        "same_name": {
                            "keyword": "돈까스",
                            "region": [],
                            "selected_region": ""
                        },
                        "total_count": 17
                    }
                }
                """;

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(KAKAO_BASE_URI + "/v2/local/search/keyword.json?query=" + keyword + "&category_group_code=FD6&radius=400&" +
                        "x=" + TEST_LONGITUDE + "&y=" + TEST_LATITUDE + "&page=1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(firstKakaoResponse, MediaType.APPLICATION_JSON));

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(KAKAO_BASE_URI + "/v2/local/search/keyword.json?query=" + keyword + "&category_group_code=FD6&radius=400&" +
                        "x=" + TEST_LONGITUDE + "&y=" + TEST_LATITUDE + "&page=2"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(secondKakaoResponse, MediaType.APPLICATION_JSON));

        //when
        NearbyRestaurantsResponseDto nearbyRestaurantsResponseDto = kakaoMapClient.findNearbyRestaurantsByKeyword(TEST_LONGITUDE, TEST_LATITUDE, keyword);

        //then
        assertAll(
                () -> assertNotNull(nearbyRestaurantsResponseDto),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getRestaurantName()).isEqualTo("온전히소바"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getAddress()).isEqualTo("대전 유성구 장대동 281-25"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getRoadAddressName()).isEqualTo("대전 유성구 문화원로6번길 48-1"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getPhoneNumber()).isEqualTo("010-2179-0986"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getLongitude()).isEqualTo(127.33664792211643),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(0).getLatitude()).isEqualTo( 36.35978173518269),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(16).getRestaurantName()).isEqualTo("이것이국밥이다 대전유성점"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(16).getAddress()).isEqualTo("대전 유성구 장대동 270-5"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(16).getRoadAddressName()).isEqualTo("대전 유성구 유성대로 757-39"),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(16).getPhoneNumber()).isEqualTo(""),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(16).getLongitude()).isEqualTo(127.334242673825),
                () -> assertThat(nearbyRestaurantsResponseDto.getRestaurantLists().get(16).getLatitude()).isEqualTo(36.360621173302)
        );
    }
}