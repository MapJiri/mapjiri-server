package project.mapjiri.domain.restaurant.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.mapjiri.domain.restaurant.dto.RestaurantCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateResponseDto;
import project.mapjiri.domain.review.dto.ReviewCreateResponseDto;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RestaurantServiceImplTest {
    @Autowired
    RestaurantService restaurantService;

    @DisplayName("가게명, 최다 태크, 후기 목록 등 가게 상세 정보 목록을 등록합니다.")
    @Test
    void registerRestaurantInfos() {
        // given
        HashMap<String, String> tags1 = new HashMap<>();
        tags1.put("맛", "5");
        tags1.put("가성비", "10");
        tags1.put("청결", "3");

        HashMap<String, String> tags2 = new HashMap<>();
        tags1.put("친절", "20");
        tags1.put("분위기", "50");
        tags1.put("청결", "2");

        List<ReviewCreateResponseDto> reviewList1 = List.of(
                new ReviewCreateResponseDto("이 집이 최고입니다1", 100, "2025.02.05", "https://img1.kakaocdn.net/cthumb"),
                new ReviewCreateResponseDto("이 집이 최고입니다2", 20, "2025.01.02", null),
                new ReviewCreateResponseDto("이 집이 최고입니다3", 20, "2025.01.01", null),
                new ReviewCreateResponseDto("이 집이 최고입니다4", 60, "2025.01.27", null),
                new ReviewCreateResponseDto("이 집이 최고입니다5", 80, "2025.02.03", null)
        );

        List<ReviewCreateResponseDto> reviewList2 = List.of(
                new ReviewCreateResponseDto("좋아해요1", 0, "2025.02.01", null),
                new ReviewCreateResponseDto("좋아해요2", 20, "2025.02.02", null),
                new ReviewCreateResponseDto("좋아해요3", 40, "2025.02.03", "이미지 URL"),
                new ReviewCreateResponseDto("좋아해요4", 20, "2025.02.04", null),
                new ReviewCreateResponseDto("좋아해요5", 60, "2025.02.05", null),
                new ReviewCreateResponseDto("싫은데 좋아요", 60, "2025.02.05", "URL1234")
        );

        List<RestaurantCreateRequestDto> restaurantList = List.of(
                new RestaurantCreateRequestDto("최강돈까스", "대전 둔상동 222-4 1층", tags1, reviewList1),
                new RestaurantCreateRequestDto("최강막국수", "대전 둔상동 333-3", tags2, reviewList2)
        );
        RestaurantListCreateRequestDto requestDto = new RestaurantListCreateRequestDto(restaurantList);

        // when
        RestaurantListCreateResponseDto result = restaurantService.registerRestaurantInfos(requestDto);

        // then
        assertThat(result.getList()).hasSize(2);

        assertThat(result.getList().get(0).getRestaurantId()).isNotNull();
        assertThat(result.getList().get(0).getReviewIds()).hasSize(5);

        assertThat(result.getList().get(1).getRestaurantId()).isNotNull();
        assertThat(result.getList().get(1).getReviewIds()).hasSize(6);

    }

}