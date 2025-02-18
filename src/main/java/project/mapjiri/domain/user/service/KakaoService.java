package project.mapjiri.domain.user.service;

import jakarta.transaction.Transactional;
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
import project.mapjiri.domain.user.dto.KakaoLoginDto;
import project.mapjiri.domain.user.dto.response.KakaoTokenResponseDto;
import project.mapjiri.domain.user.dto.response.KakaoUserInfoResponseDto;
import project.mapjiri.domain.user.model.LoginType;
import project.mapjiri.domain.user.model.Role;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.provider.JwtTokenProvider;
import project.mapjiri.domain.user.repository.UserRepository;
import project.mapjiri.global.config.KakaoConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoConfig kakaoConfig;

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final String CLIENT_ID = "YOUR_KAKAO_CLIENT_ID";
    private final String REDIRECT_URI = "YOUR_KAKAO_REDIRECT_URI";

    @Transactional
    public KakaoLoginDto kakaoLogin(String code) {
        // 🔸 카카오 토큰 요청
        String accessToken = getAccessToken(code);

        // 🔸 카카오 사용자 정보 요청
        KakaoUserInfoResponseDto userInfo = getUserInfo(accessToken);

        // 🔸 사용자 회원가입 및 로그인 처리
        User user = registerOrLoginUser(userInfo);

        // 🔸 JWT 토큰 발급
        String jwtToken = jwtTokenProvider.generateToken(user);

        return new KakaoLoginDto(jwtToken, accessToken);
    }

    /**
     * 🔸 카카오 OAuth 토큰 요청
     */
    public String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // ✅ MultiValueMap을 사용하여 올바른 요청 바디 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getClientId());  // ✅ application.yml에서 가져오기
        params.add("redirect_uri", kakaoConfig.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token", HttpMethod.POST, request, KakaoTokenResponseDto.class);

        if (response.getBody() == null) {
            throw new RuntimeException("카카오 토큰 요청 실패");
        }

        return response.getBody().getAccessToken();
    }

    /**
     * 🔸 카카오 사용자 정보 요청
     */
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<KakaoUserInfoResponseDto> response = restTemplate.exchange(
                KAKAO_USER_INFO_URL, HttpMethod.GET, request, KakaoUserInfoResponseDto.class);

        return response.getBody();
    }

    /**
     * 🔸 카카오 사용자 회원가입 및 로그인 처리
     */
    public User registerOrLoginUser(KakaoUserInfoResponseDto userInfo) {
        KakaoUserInfoResponseDto.KakaoAccount kakaoAccount = userInfo.getKakaoAccount();

        Optional<User> existingUser = userRepository.findByKakaoId(userInfo.getId().toString());

        return existingUser.orElseGet(() -> {
            User newUser = User.builder()
                    .kakaoId(userInfo.getId().toString())
                    .username(kakaoAccount.getProfile().getNickName())
                    .profileImage(kakaoAccount.getProfile().getProfileImageUrl())
                    .email(kakaoAccount.getEmail())
                    .loginType(LoginType.KAKAO)
                    .role(Role.USER)
                    .password("")
                    .build();
            return userRepository.save(newUser);
        });
    }

    public String handleKakaoLogin(String code) {
        String accessToken = getAccessToken(code);
        KakaoUserInfoResponseDto userInfo = getUserInfo(accessToken);

        //사용자 회원가입 및 로그인 처리 후 User 객체 획득
        User user = registerOrLoginUser(userInfo);

        // JWT 토큰 생성 및 반환
        return jwtTokenProvider.generateToken(user);
    }

}