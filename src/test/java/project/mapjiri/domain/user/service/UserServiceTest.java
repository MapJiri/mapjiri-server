package project.mapjiri.domain.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.mapjiri.domain.user.dto.request.LogoutRequestDto;
import project.mapjiri.domain.user.dto.request.SignInRequestDto;
import project.mapjiri.domain.user.dto.request.SignUpRequestDto;
import project.mapjiri.domain.user.dto.response.SignInResponseDto;
import project.mapjiri.domain.user.dto.response.SignUpResponseDto;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.provider.JwtTokenProvider;
import project.mapjiri.domain.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private SignUpRequestDto signUpRequestDto;
    private SignInRequestDto signInRequestDto;
    private LogoutRequestDto logoutRequestDto;

    @BeforeEach
    void setup() {
        signUpRequestDto = new SignUpRequestDto(
                "test@gmail.com",
                "Password123!",
                "test"
        );

        signInRequestDto = new SignInRequestDto(
                "test@gmail.com",
                "Password123!"
        );

        logoutRequestDto = new LogoutRequestDto(
                "RefreshToken",
                "AccessToken"
        );
    }

    @Test
    void 로그인_성공() {
        String rawPassword = signInRequestDto.getPassword();  // "Password123!"

        // Bcrypt 암호화된 비밀번호 생성
        String bcryptPassword = new BCryptPasswordEncoder().encode(rawPassword);

        User mockUser = new User(
                signInRequestDto.getEmail(),
                bcryptPassword,
                "test"
        );

        lenient().when(passwordEncoder.encode(rawPassword)).thenReturn(bcryptPassword);
        lenient().when(passwordEncoder.matches(rawPassword, bcryptPassword)).thenReturn(true);

        when(userRepository.findByEmail(signInRequestDto.getEmail())).thenReturn(Optional.of(mockUser));

        when(jwtTokenProvider.createAccessToken(mockUser.getEmail())).thenReturn("AccessToken");
        when(jwtTokenProvider.createRefreshToken(mockUser.getEmail())).thenReturn("RefreshToken");

        SignInResponseDto response = userService.signIn(signInRequestDto);

        assertEquals("AccessToken", response.getAccessToken());
        assertEquals("RefreshToken", response.getRefreshToken());

        verify(redisService, times(1)).setRefreshToken(signInRequestDto.getEmail(), "RefreshToken");
    }

    @Test
    void 회원가입_성공() {
        when(userRepository.existsByEmail(signUpRequestDto.getEmail())).thenReturn(false);
        when(redisService.isMailVerified(signUpRequestDto.getEmail())).thenReturn(true);
        when(userRepository.existsByUsername(signUpRequestDto.getUsername())).thenReturn(false);

        // 암호화가 되지 않은 상태인 "encodedPassword"를 thenReturn 해도 될까?
        // 된다! Mockito의 when().thenReturn() 기능은 실제 암호화 과정과 상관 없이 강제로 반환값을 설정해주는 Mocking 기능
        // 이렇게 하면 실제 암호화 값은 아니지만 encode가 실행되었음을 확인하는 용도로 사용 가능하다~
        when(passwordEncoder.encode(signUpRequestDto.getPassword())).thenReturn("encodedPassword");

        System.out.println("encodedPassword: " + passwordEncoder.encode(signUpRequestDto.getPassword()));

        SignUpResponseDto response = userService.signUp(signUpRequestDto);

        assertEquals(signUpRequestDto.getEmail(), response.getEmail());
        assertEquals(signUpRequestDto.getUsername(), response.getUsername());

        verify(passwordEncoder, times(1)).encode(signUpRequestDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test()
    void 회원가인_이메일_중복_실패() {
        when(userRepository.existsByEmail(signUpRequestDto.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequestDto));
        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());

        verify(redisService, never()).isMailVerified(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test()
    void 회원가입_이메일_인증_실패() {
        when(userRepository.existsByEmail(signUpRequestDto.getEmail())).thenReturn(false);
        when(redisService.isMailVerified(signUpRequestDto.getEmail())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequestDto));
        assertEquals("이메일 인증이 필요합니다.", exception.getMessage());

        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test()
    void 회원가입_닉네임_중복_실패() {
        when(userRepository.existsByEmail(signUpRequestDto.getEmail())).thenReturn(false);
        when(redisService.isMailVerified(signUpRequestDto.getEmail())).thenReturn(true);
        when(userRepository.existsByUsername(signUpRequestDto.getUsername())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequestDto));
        assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void 로그아웃_성공() {
        String email = "test@gmail.com";

        when(jwtTokenProvider.getEmailfromToken(logoutRequestDto.getRefreshToken())).thenReturn(email);

        userService.logout(logoutRequestDto);

        verify(redisService, times(1)).deleteRefreshToken(email);
        verify(redisService, times(1)).addToBlacklist(logoutRequestDto.getAccessToken());
    }
}
