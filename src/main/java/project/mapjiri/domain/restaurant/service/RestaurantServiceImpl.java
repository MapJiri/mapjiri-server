package project.mapjiri.domain.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.restaurant.dto.RestaurantCreateRequestDto;
import project.mapjiri.domain.restaurant.dto.RestaurantListCreateRequestDto;
import project.mapjiri.domain.restaurant.model.RestaurantRepository;
import project.mapjiri.domain.restaurant.model.Tag;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantListCreateResponseDto registerRestaurantInfos(RestaurantListCreateRequestDto dto) {
        // 식당 저장하기
        List<RestaurantCreateRequestDto> list = dto.getList();

        List<RestaurantCreateResponseDto> result = list.stream()
                .map(restaurantInfo -> registerRestaurantInfo(restaurantInfo))
                .toList();


        return RestaurantListCreateResponseDto.from(result);
    }

    private RestaurantCreateResponseDto registerRestaurantInfo(RestaurantCreateRequestDto dto){
        // 식당 unique 키 추출
        // 식당 최다 태그 구하기

        // DB 저장

        // 리뷰 저장하기
        //가게 후기 목록 저장하기
        return null;
    }


}
