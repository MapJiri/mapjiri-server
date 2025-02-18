package project.mapjiri.domain.placeStar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mapjiri.domain.place.model.Place;
import project.mapjiri.domain.place.service.PlaceService;
import project.mapjiri.domain.placeStar.dto.request.AddPlaceStarRequest;
import project.mapjiri.domain.placeStar.model.PlaceStar;
import project.mapjiri.domain.placeStar.repository.PlaceStarRepository;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.service.UserService;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceStarService {

    private final UserService userService;
    private final PlaceService placeService;
    private final PlaceStarRepository placeStarRepository;

    // 즐겨찾기 추가 기능
    public void addPlaceStar(AddPlaceStarRequest request){

        User user = userService.findUser();
        Place place = placeService.findPlace(request.getDong());

        if (placeStarRepository.existsByUserAndPlace(user, place)) {
            throw new MyException(MyErrorCode.ALREADY_FAVORITE_PLACE);
        }

        PlaceStar placeStar = PlaceStar.of(user, place);
        placeStarRepository.save(placeStar);
    }

    // 즐겨찾기 조회 기능
    public List<String> getPlaceStar (){

        User user = userService.findUser();

        List<PlaceStar> placeStarList = placeStarRepository.findByUser(user);

        return placeStarList.stream()
                .map(placeStar -> placeStar.getPlace().getDong())// 메뉴 이름만 리스트로 변환
                .collect(Collectors.toList());
    }

    // 즐겨찾기 취소 기능
    public void delPlaceStar(String placeKeyword) {

        User user = userService.findUser();
        Place place = placeService.findPlace(placeKeyword);

        PlaceStar placeStar = placeStarRepository.findByUserAndPlace(user, place)
                .orElseThrow(() -> new MyException(MyErrorCode.NOT_FOUND_FAVORITE_PLACE));
        placeStarRepository.delete(placeStar);
    }

}
