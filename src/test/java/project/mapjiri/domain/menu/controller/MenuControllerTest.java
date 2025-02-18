package project.mapjiri.domain.menu.controller;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import project.mapjiri.domain.menu.model.Menu;
import project.module.RestDocsSupport;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends RestDocsSupport {

    private static final String BASE_MENU_URL = "/api/v1/menu";

    @Test
    void API_메뉴_타입_목록() throws Exception {
        //given
        Mockito.when(menuService.getMenuType())
                .thenReturn(List.of("한식", "양식", "일식", "중식"));

        ResultActions actions = mockMvc.perform(
                get(BASE_MENU_URL + "/type")
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("메뉴 타입 반환 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Menu")
                                .summary("메뉴 타입 반환")
                                .responseFields(
                                        fieldWithPath("data[]").description("메뉴 타입 명").type(JsonFieldType.ARRAY),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    void API_메뉴_목록() throws Exception {
        //given
        String menuType = "일식";
        Mockito.when(menuService.getMenuName(menuType))
                .thenReturn(List.of("우동", "초밥", "돈까스", "규동"));

        //when
        ResultActions actions = mockMvc.perform(
                get(BASE_MENU_URL + "/name")
                        .queryParam("menuType", menuType)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("메뉴 목록 반환 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Menu")
                                .summary("메뉴 목록 반환")
                                .queryParameters(
                                        ResourceDocumentation.parameterWithName("menuType").description("메뉴 타입").type(SimpleType.STRING)
                                )
                                .responseFields(
                                        fieldWithPath("data[]").description("메뉴 이름").type(JsonFieldType.ARRAY),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    void API_메뉴_검색() throws Exception {
        //given
        String menuName = "돈까스";
        String menuType = "일식";
        Menu mockMenu = mock(Menu.class);
        Mockito.when(mockMenu.getMenuType()).thenReturn(menuType);
        Mockito.when(menuService.findMenu(menuName)).thenReturn(mockMenu);

        //when
        ResultActions actions = mockMvc.perform(
                get(BASE_MENU_URL + "/find-type")
                        .queryParam("menuName", menuName)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(menuType))
                .andExpect(jsonPath("$.msg").value("메뉴 이름 반환 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Menu")
                                .summary("메뉴 타입 반환")
                                .queryParameters(
                                        ResourceDocumentation.parameterWithName("menuName").description("메뉴명").type(SimpleType.STRING)
                                )
                                .responseFields(
                                        fieldWithPath("data").description("메뉴 타입").type(JsonFieldType.STRING),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }
}