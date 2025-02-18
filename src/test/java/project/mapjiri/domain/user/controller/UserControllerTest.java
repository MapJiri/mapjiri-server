package project.mapjiri.domain.user.controller;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import project.mapjiri.domain.user.dto.request.*;
import project.mapjiri.domain.user.dto.response.RefreshAccessTokenResponseDto;
import project.mapjiri.domain.user.dto.response.SignInResponseDto;
import project.mapjiri.domain.user.dto.response.SignUpResponseDto;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.support.annotation.WithMockCustom;
import project.mapjiri.support.fixture.UserFixture;
import project.module.RestDocsSupport;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends RestDocsSupport {

    private final static String BASE_USER_URL = "/api/v1/user";
    private final static String TEST_ACCESS_TOKEN = "testAccessToken";
    private final static String TEST_REFRESH_TOKEN = "testRefreshToken";

    @Test
    void API_회원가입() throws Exception {
        //given
        User user = UserFixture.USER_FIXTURE_1.createUser();
        Mockito.when(userService.signUp(any(SignUpRequestDto.class)))
                .thenReturn(new SignUpResponseDto(user.getEmail(), user.getUsername()));
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(user.getEmail(), user.getPassword(), user.getUsername());

        //when
        ResultActions actions = mockMvc.perform(
                post(BASE_USER_URL + "/signup")
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .summary("회원 가입")
                                .description("- 이메일 인증 후 진행하세요.")
                                .requestSchema(Schema.schema("SignUpRequestDto"))
                                .responseSchema(Schema.schema("SignUpResponseDto"))
                                .responseFields(
                                        fieldWithPath("email").description("사용자 이메일").type(JsonFieldType.STRING),
                                        fieldWithPath("username").description("사용자 이름").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    void API_로그인() throws Exception {
        //given
        User user = UserFixture.USER_FIXTURE_1.createUser();
        SignInResponseDto signInResponseDto = new SignInResponseDto(TEST_ACCESS_TOKEN, TEST_REFRESH_TOKEN);
        Mockito.when(userService.signIn(any(SignInRequestDto.class)))
                .thenReturn(signInResponseDto);
        SignInRequestDto signInRequestDto = new SignInRequestDto(user.getEmail(), user.getPassword());

        //when
        ResultActions actions = mockMvc.perform(
                post(BASE_USER_URL + "/signin")
                        .content(objectMapper.writeValueAsString(signInRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(TEST_ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(TEST_REFRESH_TOKEN))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .summary("로그인")
                                .requestSchema(Schema.schema("SignInRequestDto"))
                                .responseSchema(Schema.schema("SignInResponseDto"))
                                .responseFields(
                                        fieldWithPath("accessToken").description("엑세스 토큰").type(JsonFieldType.STRING),
                                        fieldWithPath("refreshToken").description("재발급 토큰").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    void API_재발급() throws Exception {
        //given
        RefreshAccessTokenResponseDto refreshAccessTokenResponseDto = new RefreshAccessTokenResponseDto(TEST_ACCESS_TOKEN, TEST_REFRESH_TOKEN);
        Mockito.when(userService.refreshAccessToken(any(RefreshAccessTokenRequestDto.class)))
                .thenReturn(refreshAccessTokenResponseDto);

        RefreshAccessTokenRequestDto refreshAccessTokenRequestDto = new RefreshAccessTokenRequestDto(TEST_REFRESH_TOKEN);

        //when
        ResultActions actions = mockMvc.perform(
                post(BASE_USER_URL + "/access-token")
                        .content(objectMapper.writeValueAsString(refreshAccessTokenRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(TEST_ACCESS_TOKEN))
                .andExpect(jsonPath("$.refreshToken").value(TEST_REFRESH_TOKEN))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .summary("재발급")
                                .requestSchema(Schema.schema("RefreshAccessTokenRequestDto"))
                                .responseSchema(Schema.schema("RefreshAccessTokenResponseDto"))
                                .responseFields(
                                        fieldWithPath("accessToken").description("엑세스 토큰").type(JsonFieldType.STRING),
                                        fieldWithPath("refreshToken").description("재발급 토큰").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    @WithMockCustom(role = "USER")
    void API_로그아웃() throws Exception {
        //given
        Mockito.doNothing().when(userService).logout(any(LogoutRequestDto.class));
        LogoutRequestDto logoutRequestDto = new LogoutRequestDto(TEST_REFRESH_TOKEN, TEST_ACCESS_TOKEN);

        //when
        ResultActions actions = mockMvc.perform(
                post(BASE_USER_URL + "/logout")
                        .content(objectMapper.writeValueAsString(logoutRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .summary("로그아웃")
                                .build())
                ));
    }

    @Test
    void API_이메일_인증번호_전송() throws Exception {
        //given
        Mockito.doNothing().when(mailService).sendMail(any(MailSendRequestDto.class));
        MailSendRequestDto mailSendRequestDto = new MailSendRequestDto("test@gmail.com");

        //when
        ResultActions actions = mockMvc.perform(
                post(BASE_USER_URL + "/send")
                        .content(objectMapper.writeValueAsString(mailSendRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .summary("이메일 인증번호 전송")
                                .requestSchema(Schema.schema("MailSendRequestDto"))
                                .build())
                ));
    }

    @Test
    void API_이메일_인증번호_검증() throws Exception {
        //given
        Mockito.when(mailService.verifyCode(any(MailVerifyRequestDto.class))).thenReturn(true);
        MailVerifyRequestDto mailVerifyRequestDto = new MailVerifyRequestDto("test@gmail.com", "123456");

        //when
        ResultActions actions = mockMvc.perform(
                post(BASE_USER_URL + "/verify")
                        .content(objectMapper.writeValueAsString(mailVerifyRequestDto))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("User")
                                .summary("이메일 인증번호 전송")
                                .requestSchema(Schema.schema("MailVerifyRequestDto"))
                                .build())
                ));
    }
}