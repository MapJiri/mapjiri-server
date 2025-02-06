package project.mapjiri.domain.review.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewTagTest {


    @DisplayName("제공된 리뷰 항목 중 최다 리뷰 항목을 제공한다.")
    @Test
    void findMostFrequentTag_1() {
        // given
        String[] inputReviewTags = {
                "맛", "맛", "맛", "맛", "맛",
                "친절", "친절", "친절", "친절", "친절", "친절", "친절", "친절", "친절", "친절",
                "친절", "친절", "친절", "친절", "친절", "친절", "친절", "친절", "친절", "친절",
                "친절", "친절", "친절", "친절", "친절", "친절", "친절", "친절", "친절",
                "분위기", "분위기", "분위기", "분위기", "분위기", "분위기", "분위기",
                "주차", "주차", "주차", "주차", "주차", "주차", "주차"
        };
        // when
        ReviewTag mostFrequentTag = ReviewTag.findMostFrequentTag(inputReviewTags);

        // then
        Assertions.assertThat(mostFrequentTag).isEqualTo(ReviewTag.FRIENDLY);
    }

    @DisplayName("리뷰 태크 최소 임계값에 도달하지 않는다면 최다 태그 리뷰를 제공하지 않는다.")
    @Test
    void findMostFrequentTag_2() {
        // given
        String[] inputReviewTags = {
                "맛", "맛", "맛", "맛", "맛",
                "분위기", "분위기", "분위기", "분위기", "분위기", "분위기", "분위기",
                "주차", "주차", "주차", "주차", "주차", "주차", "주차"
        };
        // when
        ReviewTag mostFrequentTag = ReviewTag.findMostFrequentTag(inputReviewTags);

        // then
        Assertions.assertThat(mostFrequentTag).isEqualTo(ReviewTag.NO_TAG);
    }

}