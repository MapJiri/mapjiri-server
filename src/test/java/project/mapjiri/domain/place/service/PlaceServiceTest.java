package project.mapjiri.domain.place.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mapjiri.domain.place.model.Place;
import project.mapjiri.domain.place.repository.PlaceRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PlaceService placeService;

    @Test
    public void 구_목록_조회() {
        // given
        List<String> guList = Arrays.asList("중구", "동구", "서구");
        when(placeRepository.findDistinctGu()).thenReturn(guList);

        // when
        List<String> result = placeService.getGuName();

        // then
        assertThat(guList).isEqualTo(result);
    }

    @Test
    public void 동_목록_조회() {
        // given
        String guName = "중구";
        List<String> dongNames = Arrays.asList("선부동", "죽전동", "보정동");
        when(placeRepository.findDongByGu(guName)).thenReturn(dongNames);

        // when
        List<String> result = placeService.getDongName(guName);

        // then
        assertThat(dongNames).isEqualTo(result);
    }

    @Test
    public void 장소_찾기() {
        // given
        String dongName = "신봉동";
        Place place = new Place("수지구", dongName); // 필요한 필드를 설정하세요.
        when(placeRepository.findByDong(dongName)).thenReturn(Optional.of(place));

        // when
        Place result = placeService.findPlace(dongName);

        // then
        assertThat(place).isEqualTo(result);
    }
}