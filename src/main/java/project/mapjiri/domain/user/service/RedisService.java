package project.mapjiri.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;      // 7일

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
}