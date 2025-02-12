package project.mapjiri.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.mapjiri.domain.user.dto.request.LogoutRequestDto;
import project.mapjiri.domain.user.dto.request.RefreshAccessTokenRequestDto;
import project.mapjiri.domain.user.dto.request.SignInRequestDto;
import project.mapjiri.domain.user.dto.request.SignUpRequestDto;
import project.mapjiri.domain.user.dto.response.RefreshAccessTokenResponseDto;
import project.mapjiri.domain.user.dto.response.SignInResponseDto;
import project.mapjiri.domain.user.dto.response.SignUpResponseDto;
import project.mapjiri.domain.user.provider.JwtTokenProvider;
import project.mapjiri.domain.user.service.RedisService;
import project.mapjiri.domain.user.service.UserService;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto request) throws AuthenticationException {
        SignUpResponseDto response = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto request) throws AuthenticationException {
        SignInResponseDto response = userService.signIn(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/access-token")
    public ResponseEntity<RefreshAccessTokenResponseDto> refreshAccessToken(@RequestBody RefreshAccessTokenRequestDto request) throws AuthenticationException {
        RefreshAccessTokenResponseDto response = userService.refreshAccessToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequestDto request) {
        userService.logout(request);
    }
}
