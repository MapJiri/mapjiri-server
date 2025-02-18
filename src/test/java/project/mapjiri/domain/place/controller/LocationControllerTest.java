package project.mapjiri.domain.place.controller;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import project.mapjiri.domain.place.dto.AddressResponseDto;
import project.mapjiri.domain.place.model.Place;
import project.mapjiri.global.client.dto.KakaoAddressResponseDto;
import project.module.RestDocsSupport;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LocationControllerTest extends RestDocsSupport {

    private static final String BASE_LOCATIONS_URL = "/api/v1/locations";
    private static final Double TEST_LONGITUDE = 127.3346;
    private static final Double TEST_LATITUDE = 36.3588;

    @Test
    void API_좌표로_주소_조회() throws Exception {
        //given
        AddressResponseDto addressResponseDto = AddressResponseDto.from(new KakaoAddressResponseDto(
                List.of(new KakaoAddressResponseDto.Documents("H", "대전광역시 유성구 온천2동", "유성구", "온천2동", 127.33338051944209, 36.365375474818876))
        ));

        Mockito.when(kakaoMapClient.getAddressFromCoordinate(TEST_LONGITUDE, TEST_LATITUDE))
                .thenReturn(addressResponseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get(BASE_LOCATIONS_URL + "/reverse-geocode")
                        .queryParam("longitude", String.valueOf(TEST_LONGITUDE))
                        .queryParam("latitude", String.valueOf(TEST_LATITUDE))
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.address").value("대전광역시 유성구 온천2동"))
                .andExpect(jsonPath("$.data.gu").value("유성구"))
                .andExpect(jsonPath("$.data.dong").value("온천2동"))
                .andExpect(jsonPath("$.data.longitude").value(127.33338051944209))
                .andExpect(jsonPath("$.data.latitude").value(36.365375474818876))
                .andExpect(jsonPath("$.msg").value("좌표 값 주소 변경 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Place")
                                .summary("좌표 값을 행정동 기반 주소로 변경")
                                .requestSchema(Schema.schema("AddressResponseDto"))
                                .responseFields(
                                        fieldWithPath("data.address").description("주소").type(JsonFieldType.STRING),
                                        fieldWithPath("data.gu").description("구").type(JsonFieldType.STRING),
                                        fieldWithPath("data.dong").description("행정동").type(JsonFieldType.STRING),
                                        fieldWithPath("data.longitude").description("x 좌표").type(JsonFieldType.NUMBER),
                                        fieldWithPath("data.latitude").description("y 좌표").type(JsonFieldType.NUMBER),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build())
                ));
    }

    @Test
    void API_구_목록_조회() throws Exception{
        //given
        Mockito.when(placeService.getGuName())
                .thenReturn(List.of("동구", "대덕구", "서구", "유성구", "중구"));

        //when
        ResultActions actions = mockMvc.perform(
                get(BASE_LOCATIONS_URL + "/gu")
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("구 목록 반환 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Place")
                                .summary("대전 구 목록")
                                .responseFields(
                                        fieldWithPath("data").description("구 목록").type(JsonFieldType.ARRAY),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build()
                        )
                ));
    }

    @Test
    void API_특정_구의_행정동_목록_조회() throws Exception{
        //given
        String gu = "유성구";
        Mockito.when(placeService.getDongName(gu))
                .thenReturn(List.of("온천1동", "온천2동"));

        //when
        ResultActions actions = mockMvc.perform(
                get(BASE_LOCATIONS_URL + "/dong")
                        .queryParam("gu", gu)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("동 목록 반환 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Place")
                                .summary("대전 구 목록")
                                .queryParameters(ResourceDocumentation.parameterWithName("gu").description("대전 구 이름").type(SimpleType.STRING)

                                )
                                .responseFields(
                                        fieldWithPath("data").description("행정동 목록").type(JsonFieldType.ARRAY),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build()
                        )
                ));
    }

    @Test
    void API_특정_행정동의_구_조회() throws Exception {
        //given
        String dong = "온천2동";
        String gu = "유성구";
        Place mockPlace = mock(Place.class);
        Mockito.when(mockPlace.getGu()).thenReturn(gu);

        Mockito.when(placeService.findPlace(dong)).thenReturn(mockPlace);

        //when
        ResultActions actions = mockMvc.perform(
                get(BASE_LOCATIONS_URL + "/find-gu")
                        .queryParam("dong", dong)
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.msg").value("구 이름 반환 성공"))
                .andDo(restDocsHandler.document(
                        ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                                .tag("Place")
                                .summary("행정동으로 구 조회")
                                .queryParameters(ResourceDocumentation.parameterWithName("dong").description("행정동 명").type(SimpleType.STRING)

                                )
                                .responseFields(
                                        fieldWithPath("data").description("구 이름").type(JsonFieldType.STRING),
                                        fieldWithPath("msg").description("성공 응답 메세지").type(JsonFieldType.STRING)
                                ).build()
                        )
                ));
    }

}