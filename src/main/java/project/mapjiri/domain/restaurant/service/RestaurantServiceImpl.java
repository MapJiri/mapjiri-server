package project.mapjiri.domain.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.restaurant.dto.RestaurantCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantCreateResponseDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateResponseDto;
import project.mapjiri.domain.restaurant.model.Restaurant;
import project.mapjiri.domain.restaurant.model.RestaurantRepository;
import project.mapjiri.domain.restaurant.model.Tag;
import project.mapjiri.domain.review.dto.ReviewCreateResponseDto;
import project.mapjiri.domain.review.model.Review;
import project.mapjiri.domain.review.repository.ReviewRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public RestaurantListCreateResponseDto registerRestaurantInfos(RestaurantListCreateRequestDto dto) {
        List<RestaurantCreateRequestDto> list = dto.getList();


        List<RestaurantCreateResponseDto> response = new ArrayList<>();
        for(RestaurantCreateRequestDto singleDto : list) {
            String restaurantUniqueKey = singleDto.getName() + "/" + singleDto.getAddress();
            // 식당 최다 태그 구하기
            Tag topTag = singleDto.getTags().entrySet().stream()
                    .filter(entry -> entry.getValue().matches("\\d+")) // 양의 정수만 필터링
                    .map(entry -> Map.entry(entry.getKey(), Integer.parseInt(entry.getValue()))) // 변환
                    .max(Map.Entry.comparingByValue()) // 최대값 찾기
                    .map(entry -> Tag.of(entry.getKey(), entry.getValue()))
                    .orElse(null);
            // 식당 정보 저장
            Restaurant restaurant = Restaurant.of(restaurantUniqueKey, topTag);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);

            // 식당 후기 정보 저장
            List<ReviewCreateResponseDto> reviewsDtoList = singleDto.getReviews();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            List<Review> reviews = new ArrayList<>();
            for(ReviewCreateResponseDto singleReviewDto : reviewsDtoList){
                Review review = Review.of(singleReviewDto.getReviewText(), singleReviewDto.getRating(), LocalDate.parse(singleReviewDto.getDate(), formatter), singleReviewDto.getPhotoUrl(), savedRestaurant);
                reviews.add(review);
            }
            List<Review> savedReviews = reviewRepository.saveAll(reviews);
            response.add(RestaurantCreateResponseDto.of(savedRestaurant.getRestaurantId(),
                    savedReviews.stream()
                            .map(Review::getReviewId)
                            .toList()));
        }

        return RestaurantListCreateResponseDto.from(response);
    }

}
