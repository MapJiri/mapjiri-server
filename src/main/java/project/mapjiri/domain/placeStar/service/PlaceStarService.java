//package project.mapjiri.domain.placeStar.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import project.mapjiri.domain.place.model.Place;
//import project.mapjiri.domain.placeStar.dto.PlaceStarRequest;
//import project.mapjiri.domain.placeStar.model.PlaceStar;
//import project.mapjiri.domain.placeStar.repository.PlaceStarRepository;
//import project.mapjiri.domain.user.model.User;
//import project.mapjiri.global.dto.ResponseDto;
//import project.mapjiri.global.exception.MyErrorCode;
//import project.mapjiri.global.exception.MyException;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PlaceStarService {
//
//    private final UserService userService;
//    private final PlaceService placeService;
//    private final PlaceStarRepository placeStarRepository;
//
//    // 즐겨찾기 추가 기능
//    public ResponseDto<Long> addPlaceStar(PlaceStarRequest request){
//
//        User user = userService.findUser();
//        Place place = placeService.findPlace(request.getDong());
//
//        if (placeStarRepository.existsByUserAndPlace(user, place)) {
//            throw new MyException(MyErrorCode.ALREADY_FAVORITE_PLACE);
//        }
//
//        PlaceStar placeStar = PlaceStar.of(user, place);
//        placeStarRepository.save(placeStar);
//
//        return ResponseDto.of(place.getPlaceId(), "즐겨찾기 추가 성공");
//    }
//
//    // 즐겨찾기 조회 기능
//    public ResponseDto<List<PlaceStar>> getPlaceStar (){
//
//        User user = userService.findUser();
//
//        List<PlaceStar> placeStarList = placeStarRepository.findByUser(user);
//
//        return ResponseDto.of(placeStarList,"즐겨찾기 조회 성공");
//    }
//
//    // 즐겨찾기 취소 기능
//    public ResponseDto<Void> delPlaceStar(String placeKeyword) {
//
//        User user = userService.findUser();
//        Place place = placeService.findPlace(placeKeyword);
//
//        PlaceStar placeStar = placeStarRepository.findByUserAndPlace(user, place)
//                .orElseThrow(() -> new MyException(MyErrorCode.NOT_FOUND_FAVORITE_PLACE));
//        placeStarRepository.delete(placeStar);
//
//        return ResponseDto.of(null, "즐겨찾기 취소 성공");
//    }
//
//}
