package project.mapjiri.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.user.dto.request.SignInRequestDto;
import project.mapjiri.domain.user.dto.request.SignUpRequestDto;
import project.mapjiri.domain.user.dto.response.SignInResponseDto;
import project.mapjiri.domain.user.dto.response.SignUpResponseDto;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.provider.JwtTokenProvider;
import project.mapjiri.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String username = requestDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        String password = requestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, encodedPassword, username);
        userRepository.save(user);

        return new SignUpResponseDto(user.getEmail(), user.getUsername());
    }

    public SignInResponseDto signIn(SignInRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        String password = user.getPassword();
        // 암호화된 비밀번호
        String encodedPassword = passwordEncoder.encode(password);

        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        return new SignInResponseDto(accessToken, refreshToken);
    }
}
