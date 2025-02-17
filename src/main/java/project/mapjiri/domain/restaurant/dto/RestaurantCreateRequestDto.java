package project.mapjiri.domain.restaurant.dto;

import lombok.Getter;
import project.mapjiri.domain.review.dto.ReviewCreateResponseDto;

import java.util.List;

@Getter
public class RestaurantCreateRequestDto {
    private String name;
    private List<String> tags;
    private List<ReviewCreateResponseDto> reviews;
}
