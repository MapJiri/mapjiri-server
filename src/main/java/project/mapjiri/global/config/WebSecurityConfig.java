package project.mapjiri.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import project.mapjiri.domain.user.filter.JwtAuthenticationFilter;
import project.mapjiri.domain.user.provider.JwtTokenProvider;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOriginPattern("*"); // 모든 도메인 허용
                    config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
                    config.addAllowedHeader("*"); // 모든 헤더 허용
                    config.setAllowCredentials(true); // 쿠키 허용
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/api/v1/auth/kakao/**",
                                "/api/v1/user/signin",
                                "/api/v1/user/signup"
                                ,"/h2-console/**",
                                "/swagger-ui/**","/v3/api-docs/**"
                        ).permitAll()  // 카카오 로그인 관련 엔드포인트 모두 허용
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
