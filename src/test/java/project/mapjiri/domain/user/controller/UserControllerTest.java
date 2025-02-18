package project.mapjiri.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import project.mapjiri.domain.user.dto.request.LogoutRequestDto;
import project.mapjiri.domain.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("JWT 로그아웃 테스트")
    @WithMockUser // 테스트에서 인증된 사용자로 실행
    void logoutTest() throws Exception {
        // 가짜 JWT 토큰
        String fakeJwt = "fake-jwt-token"; // "Bearer " 제거한 상태

        // LogoutRequestDto 객체 생성
        LogoutRequestDto logoutRequest = new LogoutRequestDto(fakeJwt);

        // UserService에서 로그아웃 로직을 실행하도록 Mock 설정
        doNothing().when(userService).logout(any(LogoutRequestDto.class));

        // 로그아웃 API 요청
        mockMvc.perform(post("/api/v1/user/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutRequest))) // JSON 형식으로 변환
                .andExpect(status().isOk());
    }
}

