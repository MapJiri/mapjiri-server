package project.mapjiri.domain.restaurant.dto;

import lombok.Getter;
import project.mapjiri.domain.review.dto.ReviewCreateResponseDto;

import java.util.List;
import java.util.Map;

@Getter
public class RestaurantCreateRequestDto {
    private String name;
    private String address;
    private Map<String, String> tags;
    private List<ReviewCreateResponseDto> reviews;
}
