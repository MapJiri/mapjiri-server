package project.mapjiri.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.mapjiri.domain.review.model.Review;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReviewResponse {
    private String content;
    private int score;
    private LocalDate date;
    private String imageUrl;

    private ReviewResponse(String content, int score, LocalDate date, String imageUrl) {
        this.content = content;
        this.score = score;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(review.getReviewContent(), review.getReviewPoint(), review.getDate(), review.getReviewImageUrl());
    }
}
