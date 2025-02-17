package project.mapjiri.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewCreateResponseDto {
    private String reviewText;
    private int rating;
    private String date;
    private String photoUrl;
}
