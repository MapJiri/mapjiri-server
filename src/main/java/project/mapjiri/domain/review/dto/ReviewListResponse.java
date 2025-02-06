package project.mapjiri.domain.review.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewListResponse {

    private double averageScore;
    private List<ReviewResponse> responseList;

    private ReviewListResponse(double averageScore, List<ReviewResponse> responseList) {
        this.responseList = responseList;
        this.averageScore = averageScore;
    }

    public static ReviewListResponse of(double totalScore, List<ReviewResponse> responseList) {
        return new ReviewListResponse(totalScore, responseList);
    }
}
