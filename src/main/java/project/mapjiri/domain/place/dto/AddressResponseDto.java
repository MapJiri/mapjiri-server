package project.mapjiri.domain.place.dto;

import lombok.Builder;
import lombok.Getter;
import project.mapjiri.global.client.dto.KakaoAddressResponseDto;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

@Getter
public class AddressResponseDto {
    private final String address;
    private final String gu;
    private final String dong;
    private final Double longitude;
    private final Double latitude;

    @Builder
    private AddressResponseDto(String address, String gu, String dong, Double longitude, Double latitude) {
        this.address = address;
        this.gu = gu;
        this.dong = dong;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static AddressResponseDto from(KakaoAddressResponseDto kakaoAddressResponseDto) {
        if (kakaoAddressResponseDto == null || kakaoAddressResponseDto.getDocuments() == null) {
            throw new MyException(MyErrorCode.INVALID_REQUEST);
        }
        KakaoAddressResponseDto.Documents document = kakaoAddressResponseDto.getDocuments().stream()
                .filter(documents -> documents.getRegionType().equals("H"))
                .findFirst()
                .orElseThrow(() -> new MyException(MyErrorCode.INTERNAL_SERVER_ERROR));

        return AddressResponseDto.builder()
                .address(document.getAddressName())
                .gu(document.getGu())
                .dong(document.getDong())
                .longitude(document.getLongitude())
                .latitude(document.getLatitude())
                .build();
    }
}