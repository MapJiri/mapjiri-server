package project.mapjiri.domain.menustar.controller;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import project.mapjiri.domain.menustar.dto.request.AddMenuStarRequest;
import project.mapjiri.support.annotation.WithMockCustom;
import project.module.RestDocsSupport;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuStarControllerTest extends RestDocsSupport {

    private static final String BASE_MENU_STAR_URL = "/api/v1/star/menu";

    @Test
    @WithMockCustom(role = "USER")
    void API_메뉴_즐겨찾기_추가() throws Exception{
        //given
        AddMenuStarRequest addMenuStarRequest = new AddMenuStarRequest();
        addMenuStarRequest.setMenuKeyword("돈까스");

        Mockito.doNothing().when(menustarService).addMenuStar(any(AddMenuStarRequest.class));

        //when
        ResultActions actions = mockMvc.perform(
                post(BASE_MENU_STAR_URL)
                        .content(objectMapper.writeValueAsString(addMenuStarRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.msg").value("즐겨찾기 추가 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("MenuStar")
                                .summary("메뉴 즐겨찾기 추가")
                                .requestSchema(Schema.schema("AddMenuStarRequest"))
                                .responseFields(
                                        fieldWithPath("data").description("응답 데이터 (null)").type(JsonFieldType.NULL),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    @WithMockCustom(role = "USER")
    void API_메뉴_즐겨찾기_조회() throws Exception{
        //given

        Mockito.when(menustarService.getMenuStar())
                .thenReturn(List.of("돈까스", "짜장면", "피자", "삼겹살"));

        //when
        ResultActions actions = mockMvc.perform(
                get(BASE_MENU_STAR_URL)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("즐겨찾기 목록 조회 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("MenuStar")
                                .summary("메뉴 즐겨찾기 조회")
                                .responseFields(
                                        fieldWithPath("data[]").description("즐겨찾는 음식").type(JsonFieldType.ARRAY),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    @WithMockCustom(role = "USER")
    void API_메뉴_즐겨찾기_취소() throws Exception{
        //given
        String menuKeyword = "돈까스";
        Mockito.doNothing().when(placestarService).delPlaceStar(any(String.class));

        //when
        ResultActions actions = mockMvc.perform(
                delete(BASE_MENU_STAR_URL)
                        .queryParam("menuKeyword", menuKeyword)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.msg").value("즐겨찾기 삭제 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("MenuStar")
                                .summary("메뉴 즐겨찾기 삭제")
                                .queryParameters(
                                        ResourceDocumentation.parameterWithName("menuKeyword").description("삭제할 음식").type(SimpleType.STRING)
                                )
                                .responseFields(
                                        fieldWithPath("data").description("응답 데이터 (null)").type(JsonFieldType.NULL),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }
}