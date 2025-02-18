package project.mapjiri.domain.review.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mapjiri.domain.review.dto.ReviewListResponse;
import project.mapjiri.domain.review.dto.ReviewResponse;
import project.mapjiri.domain.review.model.Review;
import project.mapjiri.domain.review.model.ReviewSort;
import project.mapjiri.domain.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    private static final int REVIEWS_PER_PAGE = 15;

    @Override
    public ReviewListResponse getReviewsByRestaurant(Long id, String sort, int pageNumber) {
        ReviewSort reviewSort = ReviewSort.fromString(sort);
        PageRequest pageRequest = PageRequest.of(
                pageNumber, REVIEWS_PER_PAGE,
                Sort.by(reviewSort.getDirection(), reviewSort.getField()));

        Page<Review> page = reviewRepository.findReviewsByRestaurantId(id, pageRequest);
        Double averageReviewScore = reviewRepository.findAverageReviewPointByRestaurantId(id)
                .orElse(0.0);

        return ReviewListResponse.of(
                averageReviewScore,
                page.stream().map(ReviewResponse::from)
                        .toList());
    }

}
