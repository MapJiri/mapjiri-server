package project.mapjiri.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60; // 7일
    private final long CODE_EXPIRATION = 5 * 60;                    // 5분
    private final long VERIFY_MAIL_EXPIRATION = 24 * 60 * 60;       // 24시간

    // RefreshToken 저장
    public void setRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(email, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.SECONDS);
    }

    // RefreshToken 조회
    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String email) {
        redisTemplate.delete(email);
    }

    // AccessToken 블랙 리스트 저장
    public void addToBlacklist(String accessToken) {
        redisTemplate.opsForValue().set(accessToken, "BLACKLIST", REFRESH_TOKEN_EXPIRATION, TimeUnit.SECONDS);
    }

    // Mail Code 저장
    public void setCode(String mail, String code) {
        String key = "code: " + mail;
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRATION, TimeUnit.SECONDS);
    }

    // Mail Code 조회
    public String getCode(String mail) {
        String key = "code: " + mail;
        return redisTemplate.opsForValue().get(key);
    }

    // Mail Code 삭제 (사용 후 제거)
    public void deleteCode(String email) {
        String key = "code:" + email;
        redisTemplate.delete(key);
    }

    // 인증 완료된 이메일 저장 (24시간 유지)
    public void setVerifiedMail(String mail) {
        String key = "verified_Mails: " + mail;
        redisTemplate.opsForValue().set(key, "true", VERIFY_MAIL_EXPIRATION, TimeUnit.SECONDS);
    }

    // 이메일 인증 여부 확인
    public boolean isMailVerified(String mail) {
        String key = "verified_Mails: " + mail;
        return redisTemplate.opsForValue().get(key) != null;
    }
}