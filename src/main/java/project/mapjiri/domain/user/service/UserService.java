package project.mapjiri.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new MyException(MyErrorCode.ALREADY_EMAIL);
        }

        if (!redisService.isMailVerified(email)) {
            throw new MyException(MyErrorCode.EMAIL_VERIFICATION_REQUIRED);
        }

        String username = requestDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new MyException(MyErrorCode.DUPLICATE_USERNAME);
        }

        String password = requestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, encodedPassword, username);
        userRepository.save(user);

        return new SignUpResponseDto(user.getEmail(), user.getUsername());
    }

    public SignInResponseDto signIn(SignInRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MyException(MyErrorCode.NOT_FOUND_EMAIL));

        // 사용자가 입력한 패스워드
        String rawPassword = request.getPassword();
        // 회원가입 할 때 입력한 패스워드 (암호화되어 있는 상태)
        String storedPassword = user.getPassword();

        if (!passwordEncoder.matches(rawPassword, storedPassword)) {
            throw new MyException(MyErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        redisService.setRefreshToken(user.getEmail(), refreshToken);

        return new SignInResponseDto(accessToken, refreshToken);
    }

    public RefreshAccessTokenResponseDto refreshAccessToken(RefreshAccessTokenRequestDto request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new MyException(MyErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtTokenProvider.getEmailfromToken(refreshToken);
        String getRefreshToken = redisService.getRefreshToken(email);

        if (getRefreshToken == null || !getRefreshToken.equals(refreshToken)) {
            throw new MyException(MyErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email);

        return new RefreshAccessTokenResponseDto(newAccessToken, getRefreshToken);
    }

    public void logout(@RequestBody LogoutRequestDto request) {
        String refreshToken = request.getRefreshToken();
        String accessToken = request.getAccessToken();

        // RefreshToken 삭제
        String email = jwtTokenProvider.getEmailfromToken(refreshToken);
        redisService.deleteRefreshToken(email);

        // AccessToken 블랙 리스트 추가
        redisService.addToBlacklist(accessToken);
    }

    public User findUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new MyException(MyErrorCode.UNAUTHENTICATED_USER);
        }

        return (User) authentication.getPrincipal();
    }
}
