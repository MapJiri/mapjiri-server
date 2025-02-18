package project.mapjiri.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.mapjiri.domain.user.dto.response.KakaoUserInfoResponseDto;
import project.mapjiri.domain.user.service.KakaoService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code) {
        log.info("카카오 인가 코드 수신: {}", code);
        String jwtToken = kakaoService.handleKakaoLogin(code);
        log.info("JWT 토큰 발급 완료: {}", jwtToken);

        return ResponseEntity.ok(jwtToken);  // 클라이언트에서 받아서 저장 후 사용
    }
}

