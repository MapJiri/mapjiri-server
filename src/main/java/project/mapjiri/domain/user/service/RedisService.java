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

    // ✅ Refresh Token 저장
    public void setRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(email, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.SECONDS);
    }

    // ✅ Refresh Token 조회 (재발급 시 사용)
    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    // ✅ Refresh Token 삭제 (로그아웃 시 사용)
    public void deleteRefreshToken(String email) {
        redisTemplate.delete(email);
    }
}