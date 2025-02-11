package project.mapjiri.domain.review.service;

import project.mapjiri.domain.review.dto.ReviewListResponse;

public interface ReviewService {

    ReviewListResponse getReviewsByRestaurant(Long id, String sort, int pageNumber);
}
