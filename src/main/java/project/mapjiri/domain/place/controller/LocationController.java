package project.mapjiri.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.mapjiri.domain.place.dto.AddressResponseDto;
import project.mapjiri.domain.place.service.PlaceService;
import project.mapjiri.global.client.KakaoMapClient;
import project.mapjiri.global.dto.ResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final KakaoMapClient kakaoMapClient;
    private final PlaceService placeService;

    @GetMapping("/reverse-geocode")
    public ResponseEntity<ResponseDto<AddressResponseDto>> coordinateToAddress(@RequestParam Double longitude, @RequestParam Double latitude) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(kakaoMapClient.getAddressFromCoordinate(longitude, latitude), "좌표 값 주소 변경 성공"));
    }

    @GetMapping("/gu")
    public ResponseEntity<ResponseDto<List<String>>> getGuName(){
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getGuName());
    }

    @GetMapping("/dong")
    public ResponseEntity<ResponseDto<List<String>>> getDongName(@RequestParam("gu") String gu){
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getDongName(gu));
    }

}
