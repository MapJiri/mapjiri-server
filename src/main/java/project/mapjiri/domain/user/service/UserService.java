package project.mapjiri.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.mapjiri.domain.user.dto.request.LogoutRequestDto;
import project.mapjiri.domain.user.dto.request.RefreshAccessTokenRequestDto;
import project.mapjiri.domain.user.dto.request.SignInRequestDto;
import project.mapjiri.domain.user.dto.request.SignUpRequestDto;
import project.mapjiri.domain.user.dto.response.RefreshAccessTokenResponseDto;
import project.mapjiri.domain.user.dto.response.SignInResponseDto;
import project.mapjiri.domain.user.dto.response.SignUpResponseDto;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.provider.JwtTokenProvider;
import project.mapjiri.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Autowired
    private final RestTemplate restTemplate; //카카오 로그아웃 API호출용

    private static final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String username = requestDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        String password = requestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, encodedPassword, username);
        userRepository.save(user);

        return new SignUpResponseDto(user.getEmail(), user.getUsername());
    }

    public SignInResponseDto signIn(SignInRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        String password = user.getPassword();
        // 암호화된 비밀번호
        String encodedPassword = passwordEncoder.encode(password);

        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getEmail());

        redisService.setRefreshToken(user.getEmail(), refreshToken);

        return new SignInResponseDto(accessToken, refreshToken);
    }

    public RefreshAccessTokenResponseDto refreshAccessToken(RefreshAccessTokenRequestDto request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }

        String email = jwtTokenProvider.getEmailfromToken(refreshToken);
        String getRefreshToken = redisService.getRefreshToken(email);

        if (getRefreshToken == null || !getRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("이미 만료된 Refresh Token 입니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email);

        return new RefreshAccessTokenResponseDto(newAccessToken, getRefreshToken);
    }

    public void logout(LogoutRequestDto logoutRequestDto) {
        String accessToken = logoutRequestDto.getAccessToken();
        if (accessToken != null) {
            redisService.deleteRefreshToken(jwtTokenProvider.getEmailfromToken(accessToken));
        }

        if (logoutRequestDto.getKakaoAccessToken() != null) {
            logoutFromKakao(logoutRequestDto.getKakaoAccessToken());
        }
    }


    private void logoutFromKakao(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_LOGOUT_URL, org.springframework.http.HttpMethod.POST, requestEntity, String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("카카오 로그아웃 실패");
        }
    }

    public SignInResponseDto autoLogin(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 Access Token 입니다.");
        }

        String email = jwtTokenProvider.getEmailfromToken(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return new SignInResponseDto(accessToken, redisService.getRefreshToken(email));
    }
}

@Configuration
class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
