package project.mapjiri.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateResponseDto {
    private String reviewText;
    private int rating;
    private String date;
    private String photoUrl;
}
