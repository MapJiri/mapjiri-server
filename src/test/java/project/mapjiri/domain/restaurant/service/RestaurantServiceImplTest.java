package project.mapjiri.domain.restaurant.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateRequestDto;
import project.mapjiri.domain.restaurant.model.RestaurantRepository;
import project.mapjiri.domain.review.repository.ReviewRepository;

@SpringBootTest
class RestaurantServiceImplTest {
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @DisplayName("")
    @Test
    void registerRestaurantInfos() {
        // given
        // when
        restaurantService.registerRestaurantInfos()
        // then
    }

}