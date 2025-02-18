package project.mapjiri.domain.restaurant.controller;

import com.epages.restdocs.apispec.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import project.mapjiri.domain.restaurant.dto.NearbyRestaurantsResponseDto;
import project.mapjiri.domain.restaurant.dto.SearchRankingResponseDto;
import project.mapjiri.global.client.dto.KakaoNearbyRestaurantResponseDto;
import project.mapjiri.support.annotation.WithMockCustom;
import project.module.RestDocsSupport;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SearchControllerTest extends RestDocsSupport {

    private static final String BABE_SEARCH_URL = "/api/v1/search";
    private static final String TEST_ACCESS_TOKEN = "Bearer testAccessToken";
    private static final Double TEST_LONGITUDE = 127.3346;
    private static final Double TEST_LATITUDE = 36.3588;

    @Test
    @WithMockCustom(role = "USER")
    void API_실시간_랭킹() throws Exception {
        //given
        Mockito.when(searchService.getRankings()).thenReturn(List.of(
                SearchRankingResponseDto.of(1, "짜장면"),
                SearchRankingResponseDto.of(2, "피자"),
                SearchRankingResponseDto.of(3, "파스타"),
                SearchRankingResponseDto.of(4, "돈까스"),
                SearchRankingResponseDto.of(5, "떡볶이"),
                SearchRankingResponseDto.of(6, "라면"),
                SearchRankingResponseDto.of(7, "삼겹살"),
                SearchRankingResponseDto.of(8, "족발"),
                SearchRankingResponseDto.of(9, "보쌈"),
                SearchRankingResponseDto.of(10, "햄버거")
        ));

        //when
        ResultActions actions = mockMvc.perform(
                get(BABE_SEARCH_URL + "/rankings")
                        .header(AUTHORIZATION, TEST_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("실시간 검색 키워드 랭킹 조회 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Search")
                                .summary("실시간 인기 검색어 목록 조회")
                                .responseSchema(Schema.schema("SearchRankingResponseDto"))
                                .responseFields(
                                        fieldWithPath("data[].ranking").description("순위").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data[].keyword").description("인기 검색어").type(JsonFieldType.STRING),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    @WithMockCustom(role = "USER")
    void API_근처_음식점_조회() throws Exception {
        //given
        Mockito.when(kakaoMapClient.findNearbyRestaurants(TEST_LONGITUDE, TEST_LATITUDE))
                .thenReturn(NearbyRestaurantsResponseDto.from(createMockDocuments()));

        //when
        ResultActions actions = mockMvc.perform(
                get(BABE_SEARCH_URL + "/nearby")
                        .param("longitude", String.valueOf(TEST_LONGITUDE))
                        .param("latitude", String.valueOf(TEST_LATITUDE))
                        .header(AUTHORIZATION, TEST_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("근처 가게 조회 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Search")
                                .summary("근처 가게 조회")
                                .responseSchema(Schema.schema("NearbyRestaurantsResponseDto"))
                                .queryParameters(
                                        ResourceDocumentation.parameterWithName("longitude").description("사용자 현재 경도").type(SimpleType.NUMBER),
                                        ResourceDocumentation.parameterWithName("latitude").description("사용자 현재 위도").type(SimpleType.NUMBER)
                                )
                                .responseFields(
                                        fieldWithPath("data.totalCount").description("조회된 음식점 수").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data.restaurantLists[].restaurantName").description("음식점 이름").type(JsonFieldType.STRING),
                                        fieldWithPath("data.restaurantLists[].address").description("음식점 주소").type(JsonFieldType.STRING),
                                        fieldWithPath("data.restaurantLists[].roadAddressName").description("음식점 도로명 주소").type(JsonFieldType.STRING),
                                        fieldWithPath("data.restaurantLists[].phoneNumber").description("음식점 전화 번호").type(JsonFieldType.STRING),
                                        fieldWithPath("data.restaurantLists[].longitude").description("음식점 위치 x 좌표").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data.restaurantLists[].latitude").description("음식점 위치 y 좌표").type(JsonFieldType.NUMBER),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    @WithMockCustom(role = "USER")
    void API_키워드_기반_음식점_조회 () throws Exception {
        //given
        String keyword = "돈까스";
        Mockito.when(kakaoMapClient.findNearbyRestaurantsByKeyword(TEST_LONGITUDE, TEST_LATITUDE, keyword))
                .thenReturn(NearbyRestaurantsResponseDto.from(createMockDocuments()));

        //when
        ResultActions actions = mockMvc.perform(
                get(BABE_SEARCH_URL + "/nearby")
                        .param("longitude", String.valueOf(TEST_LONGITUDE))
                        .param("latitude", String.valueOf(TEST_LATITUDE))
                        .param("keyword", keyword)
                        .header(AUTHORIZATION, TEST_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("근처 " + keyword + " 가게 조회 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Search")
                                .summary("근처 가게 조회")
                                .responseSchema(Schema.schema("NearbyRestaurantsResponseDto"))
                                .queryParameters(
                                        ResourceDocumentation.parameterWithName("longitude").description("사용자 현재 경도").type(SimpleType.NUMBER),
                                        ResourceDocumentation.parameterWithName("latitude").description("사용자 현재 위도").type(SimpleType.NUMBER),
                                        ResourceDocumentation.parameterWithName("keyword").description("검색 키워드").type(SimpleType.STRING).optional()
                                )
                                .responseFields(
                                        fieldWithPath("data.totalCount").description("조회된 음식점 수").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data.restaurantLists[].restaurantName").description("음식점 이름").type(JsonFieldType.STRING),
                                        fieldWithPath("data.restaurantLists[].address").description("음식점 주소").type(JsonFieldType.STRING),
                                        fieldWithPath("data.restaurantLists[].roadAddressName").description("음식점 도로명 주소").type(JsonFieldType.STRING),
                                        fieldWithPath("data.restaurantLists[].phoneNumber").description("음식점 전화 번호").type(JsonFieldType.STRING),
                                        fieldWithPath("data.restaurantLists[].longitude").description("음식점 위치 x 좌표").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data.restaurantLists[].latitude").description("음식점 위치 y 좌표").type(JsonFieldType.NUMBER),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    public static List<KakaoNearbyRestaurantResponseDto.Documents> createMockDocuments() {
        return Arrays.asList(
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "돈까스클럽 강남점",
                        "서울 강남구 테헤란로 123",
                        "서울 강남구 테헤란로 123 1층",
                        "02-1234-5678",
                        127.0254,
                        37.4985
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "왕돈까스 강남역점",
                        "서울 강남구 역삼동 456-7",
                        "서울 강남구 강남대로 789",
                        "02-9876-5432",
                        127.0275,
                        37.4990
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "일식 돈까스 전문점",
                        "서울 강남구 삼성동 111-22",
                        "서울 강남구 봉은사로 100",
                        "02-3456-7890",
                        127.0301,
                        37.5002
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "수제왕돈까스 홍대점",
                        "서울 마포구 서교동 567-8",
                        "서울 마포구 홍익로 12",
                        "02-8888-1234",
                        126.9234,
                        37.5565
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "크리스피돈까스 신촌점",
                        "서울 서대문구 신촌로 789",
                        "서울 서대문구 신촌로 789 2층",
                        "02-7777-5678",
                        126.9372,
                        37.5601
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "엄마손돈까스",
                        "서울 종로구 종로 100",
                        "서울 종로구 종로 100 3층",
                        "02-6666-9999",
                        126.9784,
                        37.5705
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "김치돈까스 전문점",
                        "서울 송파구 석촌호수로 456",
                        "서울 송파구 석촌호수로 456 1층",
                        "02-5555-1111",
                        127.1047,
                        37.5083
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "수제돈까스 마포점",
                        "서울 마포구 양화로 789",
                        "서울 마포구 양화로 789 2층",
                        "02-4444-2222",
                        126.9204,
                        37.5550
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "흑돼지돈까스 종로점",
                        "서울 종로구 삼청동길 200",
                        "서울 종로구 삼청동길 200 1층",
                        "02-3333-3333",
                        126.9810,
                        37.5772
                ),
                new KakaoNearbyRestaurantResponseDto.Documents(
                        "왕돈까스 명동점",
                        "서울 중구 명동길 50",
                        "서울 중구 명동길 50 2층",
                        "02-2222-4444",
                        126.9840,
                        37.5639
                )
        );
    }
}