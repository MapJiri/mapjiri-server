package project.mapjiri.domain.review.controller;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import project.mapjiri.domain.restaurant.model.Restaurant;
import project.mapjiri.domain.restaurant.model.Tag;
import project.mapjiri.domain.review.dto.ReviewListResponse;
import project.mapjiri.domain.review.dto.ReviewResponse;
import project.mapjiri.domain.review.model.Review;
import project.module.RestDocsSupport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest extends RestDocsSupport {

    private static final String BASE_REVIEW_URL = "/api/v1/review";

    @Test
    void API_음식점_리뷰_조회() throws Exception {
        //given
        String name = "식당1";
        String address = "대전 둔산동 242-5 1층";
        String uniqueKey = name + "/" + address;
        int page = 1;
        String sort = "latest";

        Restaurant restaurant = Restaurant.of(uniqueKey, Tag.of("맛", 5));
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (int per = 0; per < 10; per++) {
            Review review = Review.of("리뷰" + per, 4, LocalDate.of(2024, 3, per + 1), "null", restaurant);
            reviewResponses.add(ReviewResponse.from(review));
        }

        double averageScore = 5.0;
        ReviewListResponse reviewListResponse = ReviewListResponse.of(averageScore, reviewResponses);

        Mockito.when(reviewService.getReviewsByRestaurant(name, address, sort, page - 1))
                .thenReturn(reviewListResponse);

        //when
        ResultActions actions = mockMvc.perform(
                get(BASE_REVIEW_URL + "/restaurant")
                        .queryParam("name", name)
                        .queryParam("address", address)
                        .queryParam("sort", sort)
                        .queryParam("pageNumber", String.valueOf(page))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.responseList").isNotEmpty())
                .andExpect(jsonPath("$.data.averageScore").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("리뷰 목록 조회 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Review")
                                .summary("리뷰 목록 조회")
                                .description("""
                                        sort를 통해 정렬하세요.
                                        <br>
                                        `LATEST`= 최신순 (디폴트)
                                        `OLDEST`= 오래된순
                                        `HIGH_SCORE`= 별점높은순
                                        `LOW_SCORE`= 별점낮은순
                                        """)
                                .responseSchema(Schema.schema("reviewListResponse"))
                                        .queryParameters(
                                                ResourceDocumentation
                                                        .parameterWithName("name")
                                                        .description("가게명")
                                                        .type(SimpleType.STRING),
                                                ResourceDocumentation
                                                        .parameterWithName("address")
                                                        .description("주소")
                                                        .type(SimpleType.STRING),
                                                ResourceDocumentation
                                                        .parameterWithName("sort")
                                                        .description("정렬 조건")
                                                        .type(SimpleType.STRING)
                                                        .defaultValue("latest")
                                                        .optional(),
                                                ResourceDocumentation
                                                        .parameterWithName("pageNumber")
                                                        .description("페이지")
                                                        .type(SimpleType.NUMBER)
                                                        .defaultValue(1)
                                                        .optional()
                                        )
                                .responseFields(
                                        fieldWithPath("data.responseList[]").description("리뷰 리스트").type(JsonFieldType.ARRAY),
                                        fieldWithPath("data.averageScore").description("전체 평균 점수").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data.responseList[].content").description("리뷰").type(JsonFieldType.STRING),
                                        fieldWithPath("data.responseList[].score").description("리뷰 점수").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data.responseList[].date").description("작성일").type(JsonFieldType.STRING),
                                        fieldWithPath("data.responseList[].imageUrl").description("리뷰 사진").type(JsonFieldType.STRING),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));

    }
}