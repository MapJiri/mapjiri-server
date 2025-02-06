package project.mapjiri.domain.review.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.mapjiri.domain.restaurant.model.Restaurant;
import project.mapjiri.domain.restaurant.model.RestaurantRepository;
import project.mapjiri.domain.review.dto.ReviewListResponse;
import project.mapjiri.domain.review.dto.ReviewResponse;
import project.mapjiri.domain.review.model.Review;
import project.mapjiri.domain.review.model.ReviewTag;
import project.mapjiri.domain.review.repository.ReviewRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class ReviewServiceImplTest {
    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @DisplayName("최신순, 오래된순 등 여러 기준 별 리뷰 페이징 조회 시나리오")
    @TestFactory
    Collection<DynamicTest> getReviewsByRestaurant() {
        // given
        Restaurant restaurant1 = new Restaurant("식당1", "구주소1", "신주소1", "번호1", ReviewTag.GO, 0.1, 0.1);
        Restaurant restaurant2 = new Restaurant("식당2", "구주소2", "신주소2", "번호2", ReviewTag.GO, 0.1, 0.2);
        restaurantRepository.saveAllAndFlush(List.of(restaurant1, restaurant2));

        List<Review> reviews = new ArrayList<>();
        for (int per = 1; per <= 20; per++) {
            Review review = Review.of("리뷰" + per, 4,
                    LocalDate.of(2024, 3, per), "null", restaurant1);
            reviews.add(review);
        }
        for (int per = 21; per <= 31; per++) {
            Review review = Review.of("리뷰" + per, 3,
                    LocalDate.of(2024, 3, per), "null", restaurant1);
            reviews.add(review);
        }
        reviewRepository.saveAllAndFlush(reviews);

        // when & then
        return List.of(
                DynamicTest.dynamicTest("오래된순으로 리뷰 목록 페이지를 조회합니다.", () -> {

                    //when
                    ReviewListResponse result = reviewService.getReviewsByRestaurant(restaurant1.getRestaurantId(), "oldest", 0);
                    // then
                    List<ReviewResponse> responseList = result.getResponseList();
                    assertThat(responseList)
                            .extracting("date")
                            .containsExactly(
                                    LocalDate.of(2024, 3, 1),
                                    LocalDate.of(2024, 3, 2),
                                    LocalDate.of(2024, 3, 3),
                                    LocalDate.of(2024, 3, 4),
                                    LocalDate.of(2024, 3, 5),
                                    LocalDate.of(2024, 3, 6),
                                    LocalDate.of(2024, 3, 7),
                                    LocalDate.of(2024, 3, 8),
                                    LocalDate.of(2024, 3, 9),
                                    LocalDate.of(2024, 3, 10),
                                    LocalDate.of(2024, 3, 11),
                                    LocalDate.of(2024, 3, 12),
                                    LocalDate.of(2024, 3, 13),
                                    LocalDate.of(2024, 3, 14),
                                    LocalDate.of(2024, 3, 15)
                            );
                }),
                DynamicTest.dynamicTest("최신순으로 리뷰 목록 페이지를 조회합니다.", () -> {

                    //when
                    ReviewListResponse result = reviewService.getReviewsByRestaurant(restaurant1.getRestaurantId(), "lastest", 0);
                    // then
                    List<ReviewResponse> responseList = result.getResponseList();
                    assertThat(responseList)
                            .extracting("date")
                            .containsExactly(
                                    LocalDate.of(2024, 3, 31),
                                    LocalDate.of(2024, 3, 30),
                                    LocalDate.of(2024, 3, 29),
                                    LocalDate.of(2024, 3, 28),
                                    LocalDate.of(2024, 3, 27),
                                    LocalDate.of(2024, 3, 26),
                                    LocalDate.of(2024, 3, 25),
                                    LocalDate.of(2024, 3, 24),
                                    LocalDate.of(2024, 3, 23),
                                    LocalDate.of(2024, 3, 22),
                                    LocalDate.of(2024, 3, 21),
                                    LocalDate.of(2024, 3, 20),
                                    LocalDate.of(2024, 3, 19),
                                    LocalDate.of(2024, 3, 18),
                                    LocalDate.of(2024, 3, 17)
                            );
                }),
                DynamicTest.dynamicTest("별점 높은 순으로 리뷰 목록 페이지를 조회합니다.", () -> {

                    //when
                    ReviewListResponse result = reviewService.getReviewsByRestaurant(restaurant1.getRestaurantId(), "high_score", 1);
                    // then

                    List<ReviewResponse> responseList = result.getResponseList();
                    assertThat(responseList)
                            .extracting("score", "date")
                            .containsExactly(
                                    tuple(4, LocalDate.of(2024, 3, 16)),
                                    tuple(4, LocalDate.of(2024, 3, 17)),
                                    tuple(4, LocalDate.of(2024, 3, 18)),
                                    tuple(4, LocalDate.of(2024, 3, 19)),
                                    tuple(4, LocalDate.of(2024, 3, 20)),
                                    tuple(3, LocalDate.of(2024, 3, 21)),
                                    tuple(3, LocalDate.of(2024, 3, 22)),
                                    tuple(3, LocalDate.of(2024, 3, 23)),
                                    tuple(3, LocalDate.of(2024, 3, 24)),
                                    tuple(3, LocalDate.of(2024, 3, 25)),
                                    tuple(3, LocalDate.of(2024, 3, 26)),
                                    tuple(3, LocalDate.of(2024, 3, 27)),
                                    tuple(3, LocalDate.of(2024, 3, 28)),
                                    tuple(3, LocalDate.of(2024, 3, 29)),
                                    tuple(3, LocalDate.of(2024, 3, 30))
                            );
                })

        );


    }


}