package project.mapjiri.domain.review.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mapjiri.domain.restaurant.model.Restaurant;
import project.mapjiri.domain.restaurant.model.RestaurantRepository;
import project.mapjiri.domain.review.dto.ReviewListResponse;
import project.mapjiri.domain.review.dto.ReviewResponse;
import project.mapjiri.domain.review.model.Review;
import project.mapjiri.domain.review.model.ReviewSort;
import project.mapjiri.domain.review.repository.ReviewRepository;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    private static final int REVIEWS_PER_PAGE = 15;

    @Override
    public ReviewListResponse getReviewsByRestaurant(String name, String address, String sort, int pageNumber) {
        ReviewSort reviewSort = ReviewSort.fromString(sort);
        PageRequest pageRequest = PageRequest.of(
                pageNumber, REVIEWS_PER_PAGE,
                Sort.by(reviewSort.getDirection(), reviewSort.getField()));

        String uniqueKey = name+ "/" + address;
        Restaurant findRestaurant = restaurantRepository.findByUniqueKey(uniqueKey)
                .orElseThrow(() -> new MyException(MyErrorCode.INVALID_VERIFICATION_CODE));
        Page<Review> page = reviewRepository.findReviewsByRestaurantId(findRestaurant.getRestaurantId(), pageRequest);
        Double averageReviewScore = reviewRepository.findAverageReviewPointByRestaurantId(findRestaurant.getRestaurantId())
                .orElse(0.0);

        return ReviewListResponse.of(
                averageReviewScore,
                page.stream().map(ReviewResponse::from)
                        .toList());
    }

}
