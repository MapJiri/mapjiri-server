package project.mapjiri.domain.restaurant.controller;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import project.mapjiri.domain.restaurant.dto.RestaurantCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantCreateResponseDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateResponseDto;
import project.mapjiri.domain.review.dto.ReviewCreateResponseDto;
import project.module.RestDocsSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends RestDocsSupport {

    private static final String BASE_RESTAURANT_URL = "/api/v1/restaurant";

    @Test
    void API_음식점_정보_조회() throws Exception {
        //given

        // 요청 설정
        HashMap<String, String> tags = new HashMap<>();
        tags.put("맛", "5");
        tags.put("가성비", "10");
        tags.put("청결", "3");

        List<ReviewCreateResponseDto> reviewList = List.of(
                new ReviewCreateResponseDto("이 집이 최고입니다1", 100, "2025.02.05.", "https://img1.kakaocdn.net/cthumb"),
                new ReviewCreateResponseDto("이 집이 최고입니다2", 20, "2025.01.02.", null),
                new ReviewCreateResponseDto("이 집이 최고입니다3", 20, "2025.01.01.", null),
                new ReviewCreateResponseDto("이 집이 최고입니다4", 60, "2025.01.27.", null),
                new ReviewCreateResponseDto("이 집이 최고입니다5", 80, "2025.02.03.", null)
        );

        List<RestaurantCreateRequestDto> restaurantList = List.of(
                new RestaurantCreateRequestDto("최강돈까스", "대전 둔상동 222-4 1층", tags, reviewList)
        );
        RestaurantListCreateRequestDto requestDto = new RestaurantListCreateRequestDto(restaurantList);

        // 응답 설정
        Long restaurantId = 1L;

        List<Long> ReviewIds = new ArrayList<>();
        ReviewIds.add(1L);
        ReviewIds.add(2L);
        ReviewIds.add(3L);
        ReviewIds.add(4L);
        ReviewIds.add(5L);

        List<RestaurantCreateResponseDto> restaurantCreateResponseDtos =
                List.of(
                        RestaurantCreateResponseDto.of(restaurantId, ReviewIds)
                );

        RestaurantListCreateResponseDto responseDto = RestaurantListCreateResponseDto.from(restaurantCreateResponseDtos);


        Mockito.when(restaurantService.registerRestaurantInfos(any(RestaurantListCreateRequestDto.class)))
                .thenReturn(responseDto);

        ResultActions actions = mockMvc.perform(
                post(BASE_RESTAURANT_URL + "/info")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Restaurant")
                                .summary("음식점 상세 정보 조회")
                                .description("음식점 상세 정보를 조회합니다. 리뷰 데이터가 포함되어 있습니다.")
                                .requestSchema(Schema.schema("RestaurantListCreateRequestDto"))
                                .responseSchema(Schema.schema("RestaurantListCreateResponseDto"))
                                .responseFields(
                                        fieldWithPath("data.list[]").description("음식점 상세 정보").type(JsonFieldType.ARRAY),
                                        fieldWithPath("data.list[].restaurantId").description("음식점 PK").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data.list[].reviewIds").description("리뷰 PK 리스트").type(JsonFieldType.ARRAY),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

}