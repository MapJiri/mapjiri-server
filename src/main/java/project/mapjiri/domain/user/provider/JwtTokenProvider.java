package project.mapjiri.domain.user.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.mapjiri.domain.user.model.Role;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.repository.UserRepository;

import java.nio.channels.ScatteringByteChannel;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String secretKey;

    private final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000;                // 1시간
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;      // 7일

    private Key createKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ JWT 블랙리스트 추가 (로그아웃 시 사용)
    public void addToBlacklist(String token) {
        long expiration = getExpiration(token);
        redisTemplate.opsForValue().set(token, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    // ✅ 토큰이 블랙리스트에 있는지 확인
    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
    // ✅ 토큰 만료 시간 가져오기
    private long getExpiration(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }


    public String generateToken(User user) {
        return createAccessToken(user.getEmail());
    }

    // AccessToken 생성
    public String createAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(createKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(Long userId,String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(createKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰에서 Claims(사용자 정보) 가져오는 메서드 추가
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(createKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            if (isBlacklisted(token)) return false; // ✅ 블랙리스트 체크
            Jwts.parserBuilder()
                    .setSigningKey(createKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 이메일로 토큰 값 알아내기
    public String getEmailfromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(createKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    // 권한을 부여하는 부분
    public Authentication getAuthentication(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new UsernamePasswordAuthenticationToken(user, null, List.of(authority));
    }
}
