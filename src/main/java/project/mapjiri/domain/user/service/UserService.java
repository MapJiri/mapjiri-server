package project.mapjiri.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.user.dto.request.SignUpRequestDto;
import project.mapjiri.domain.user.dto.response.SignUpResponseDto;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}
