package project.mapjiri.domain.review.service;

import project.mapjiri.domain.review.dto.ReviewListResponse;

public interface ReviewService {

    ReviewListResponse getReviewsByRestaurant(String name, String address, String sort, int pageNumber);
}
