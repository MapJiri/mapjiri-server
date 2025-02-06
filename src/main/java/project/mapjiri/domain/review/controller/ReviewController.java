package project.mapjiri.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.mapjiri.domain.review.service.ReviewService;
import project.mapjiri.global.dto.ResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/restaurant")
    public ResponseDto<?> getReviewsByRestaurant(@RequestParam(name = "restaurantId") Long id
            , @RequestParam(defaultValue = "ASD", required = false) String sort
            , @RequestParam(defaultValue = "1", required = false) int pageNumber) {
        pageNumber = pageNumber <= 0 ? 0 : pageNumber - 1;

        return ResponseDto.of(reviewService.getReviewsByRestaurant(id, sort, pageNumber), "리뷰 목록 조회 성공");
    }
}
