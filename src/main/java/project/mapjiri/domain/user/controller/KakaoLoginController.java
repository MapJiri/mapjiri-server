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
    // public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) { //수정 전 코드
    public KakaoUserInfoResponseDto kakaoCallback(@RequestParam("code") String code) {
        log.info("카카오 인가 코드 수신: {}", code);

        // ✅ 인수 1개만 전달
        String accessToken = kakaoService.getKakaoAccessToken(code);
        log.info("카카오 액세스 토큰: {}", accessToken);

        // ✅ 인수 1개만 전달
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        log.info("카카오 사용자 정보: {}", userInfo);

        // return ResponseEntity.ok(userInfo); //수정전 코드
        return userInfo;
    }
}
