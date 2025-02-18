package project.mapjiri.domain.placeStar.controller;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import project.mapjiri.domain.placeStar.dto.request.AddPlaceStarRequest;
import project.module.RestDocsSupport;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PlaceStarControllerTest extends RestDocsSupport {

    private final static String BASE_PLACE_STAR_URL = "/api/v1/star/place";

    @Test
    void API_즐겨찾기_추가() throws Exception{
        //given
        AddPlaceStarRequest addPlaceStarRequest = new AddPlaceStarRequest();
        addPlaceStarRequest.setGu("유성구");
        addPlaceStarRequest.setDong("온천2동");

        Mockito.doNothing().when(placestarService).addPlaceStar(any(AddPlaceStarRequest.class));

        //when
        ResultActions actions = mockMvc.perform(
                post(BASE_PLACE_STAR_URL)
                        .content(objectMapper.writeValueAsString(addPlaceStarRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.msg").value("즐겨찾기 추가 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("PlaceStar")
                                .summary("즐겨찾기 추가")
                                .requestSchema(Schema.schema("AddPlaceStarRequest"))
                                .responseFields(
                                        fieldWithPath("data").description("응답 데이터 (null)").type(JsonFieldType.NULL),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }
}