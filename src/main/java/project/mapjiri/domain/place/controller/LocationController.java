package project.mapjiri.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.mapjiri.domain.place.dto.AddressResponseDto;
import project.mapjiri.global.client.KakaoMapClient;
import project.mapjiri.global.dto.ResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final KakaoMapClient kakaoMapClient;

    @GetMapping("/reverse-geocode")
    public ResponseEntity<ResponseDto<AddressResponseDto>> coordinateToAddress(@RequestParam Double longitude, @RequestParam Double latitude) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(kakaoMapClient.getAddressFromCoordinate(longitude, latitude), "좌표 값 주소 변경 성공"));
    }
}
