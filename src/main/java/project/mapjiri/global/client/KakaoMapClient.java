package project.mapjiri.global.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import project.mapjiri.global.client.dto.KakaoAddressResponseDto;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;


@Slf4j
@Component
public class KakaoMapClient {

    private final RestClient restClient;

    public KakaoMapClient(RestClient kakaoRestClient) {
        this.restClient = kakaoRestClient;
    }


    public KakaoAddressResponseDto getAddressFromCoordinate(Double longitude, Double latitude) {
        String uri = UriComponentsBuilder.fromPath("/geo/coord2regioncode.json")
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .toUriString();
        return sendGetRequest(uri).body(KakaoAddressResponseDto.class);
    }

    private RestClient.ResponseSpec sendGetRequest(String uri) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.error("외부 서버(카카오) 잚못된 요청 값");
                    throw new MyException(MyErrorCode.INVALID_REQUEST);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    log.error("외부 서버(카카오) 오류 발생");
                    throw new MyException(MyErrorCode.BAD_GATEWAY);
                });
    }
}
