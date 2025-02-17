package project.mapjiri.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import project.mapjiri.domain.user.dto.response.KakaoUserInfoResponseDto;
import project.mapjiri.global.config.KakaoConfig;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 카카오 로그인 - 인가 코드로 액세스 토큰 요청 후 사용자 정보 반환
     */
    public KakaoUserInfoResponseDto kakaoLogin(String code) {
        String accessToken = getKakaoAccessToken(code);
        return getUserInfo(accessToken);
    }

    /**
     * 카카오 액세스 토큰 요청
     */
    public String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getClientId());
        params.add("redirect_uri", kakaoConfig.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getBody() == null || !response.getBody().containsKey("access_token")) {
            throw new RuntimeException("카카오 액세스 토큰 발급 실패");
        }

        return response.getBody().get("access_token").toString();
    }

    /**
     * 카카오 사용자 정보 가져오기
     */
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoResponseDto> response = restTemplate.exchange(
                userInfoUrl, HttpMethod.GET, request, KakaoUserInfoResponseDto.class);

        if (response.getBody() == null) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패");
        }

        return response.getBody(); // ✅ JSON 응답을 그대로 `KakaoUserInfoResponseDto` 객체로 변환하여 반환
    }
}



