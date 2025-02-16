package project.mapjiri.domain.placeStar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mapjiri.domain.placeStar.dto.request.AddPlaceStarRequest;
import project.mapjiri.domain.placeStar.service.PlaceStarService;
import project.mapjiri.global.dto.ResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/star/place")
public class PlaceStarController {

    private final PlaceStarService placeStarService;

    @PostMapping
    ResponseEntity<ResponseDto<Void>> addPlaceStar(@RequestBody AddPlaceStarRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.of(null,"즐겨찾기 추가 성공"));
    }

    @GetMapping
    ResponseEntity<ResponseDto<List<String>>> getPlaceStar(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(placeStarService.getPlaceStar(),"즐겨찾기 조회 성공"));
    }

    @DeleteMapping
    ResponseEntity<ResponseDto<Void>> delPlaceStar(@RequestParam("placeKeyword") String placeKeyword){
        placeStarService.delPlaceStar(placeKeyword);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(null, "즐겨찾기 삭제 성공"));
    }
}
