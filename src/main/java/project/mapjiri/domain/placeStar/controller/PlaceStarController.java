package project.mapjiri.domain.placeStar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mapjiri.domain.placeStar.dto.PlaceStarRequest;
import project.mapjiri.domain.placeStar.model.PlaceStar;
import project.mapjiri.domain.placeStar.service.PlaceStarService;
import project.mapjiri.global.dto.ResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite/place")
public class PlaceStarController {

    private final PlaceStarService placeStarService;

    @PostMapping
    ResponseEntity<ResponseDto<Long>> addPlaceStar(@RequestBody PlaceStarRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(placeStarService.addPlaceStar(request));
    }

    @GetMapping
    ResponseEntity<ResponseDto<List<PlaceStar>>> getPlaceStar(){
        return ResponseEntity.status(HttpStatus.OK).body(placeStarService.getPlaceStar());
    }

    @DeleteMapping
    ResponseEntity<ResponseDto<Void>> delPlaceStar(@RequestParam("placeKeyword") String placeKeyword){
        return ResponseEntity.status(HttpStatus.OK).body(placeStarService.delPlaceStar(placeKeyword));
    }
}
