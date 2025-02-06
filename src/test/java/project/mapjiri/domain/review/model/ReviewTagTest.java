package project.mapjiri.domain.review.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static project.mapjiri.domain.review.model.ReviewTag.*;

class ReviewTagTest {

    @DisplayName("주어진 문자열을 통해 ReviewTag 을 반환한다.")
    @Test
    void fromString_1() {
        // given
        String string = "맛";
        // when
        ReviewTag result = ReviewTag.fromString(string);
        // then
        assertThat(result).isEqualTo(TASTE);
    }

    @DisplayName("주어진 문자열이 양식에 없다면 ReviewTag UNKNOWN 을 반환한다.")
    @Test
    void fromString_2() {
        // given
        String string = "안녕하십니까!";
        // when
        ReviewTag result = ReviewTag.fromString(string);
        // then
        assertThat(result).isEqualTo(UNKNOWN);
    }


    @DisplayName("제공된 리뷰 항목 중 최다 리뷰 항목을 제공한다.")
    @Test
    void findMostFrequentTag_1() {
        // given
        List<ReviewTag> inputReviewTags = List.of(
                TASTE, TASTE, TASTE, TASTE, TASTE,
                FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY,
                FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY,
                FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY,
                AMBIENCE, AMBIENCE, AMBIENCE, AMBIENCE, AMBIENCE, AMBIENCE, AMBIENCE,
                PARKING, PARKING, PARKING, PARKING, PARKING, PARKING, PARKING
        );
        // when
        ReviewTag mostFrequentTag = findMostFrequentTag(inputReviewTags);

        // then
        assertThat(mostFrequentTag).isEqualTo(FRIENDLY);
    }

    @DisplayName("리뷰 태크 최소 임계값에 도달하지 않는다면 최다 태그 리뷰를 제공하지 않는다.")
    @Test
    void findMostFrequentTag_2() {
        // given
        List<ReviewTag> inputReviewTags = List.of(
                TASTE, TASTE, TASTE, TASTE, TASTE,
                FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY, FRIENDLY,
                PARKING, PARKING, PARKING, PARKING, PARKING, PARKING
        );
        // when
        ReviewTag mostFrequentTag = findMostFrequentTag(inputReviewTags);

        // then
        assertThat(mostFrequentTag).isEqualTo(NO_TAG);
    }

}