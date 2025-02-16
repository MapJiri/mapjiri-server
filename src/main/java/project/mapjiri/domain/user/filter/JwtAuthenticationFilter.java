package project.mapjiri.domain.user.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import project.mapjiri.domain.user.provider.JwtTokenProvider;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();

//        if (isPublicUrl(requestURI)) {
//            filterChain.doFilter(request, response); // 토큰 검사 없이 진행
//            return;
//        }

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            // 토큰 유효성 검사
            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmailfromToken(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(email);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("authentication = " + authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private static boolean isPublicUrl(final String requestURI) {

        return
                requestURI.startsWith("/");
    }
}
