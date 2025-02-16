package project.mapjiri.domain.placeStar.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mapjiri.domain.place.model.Place;
import project.mapjiri.domain.place.service.PlaceService;
import project.mapjiri.domain.placeStar.dto.request.AddPlaceStarRequest;
import project.mapjiri.domain.placeStar.model.PlaceStar;
import project.mapjiri.domain.placeStar.repository.PlaceStarRepository;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class PlaceStarServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PlaceService placeService;

    @Mock
    private PlaceStarRepository placeStarRepository;

    @InjectMocks
    private PlaceStarService placeStarService;

    @Test
    public void testAddPlaceStar() {
        // given
        AddPlaceStarRequest request = new AddPlaceStarRequest();
        request.setDong("동동동");

        User user = new User("test@email.com", "encodedPassword", "testUser");
        Place place = new Place("중구", "동동동");

        when(userService.findUser()).thenReturn(user);
        when(placeService.findPlace("동동동")).thenReturn(place);
        when(placeStarRepository.existsByUserAndPlace(user, place)).thenReturn(false);

        // when
        placeStarService.addPlaceStar(request);

        // then: PlaceStarRepository의 save()가 한 번 호출되어야 함
        verify(placeStarRepository, times(1)).save(any(PlaceStar.class));
    }

    @Test
    public void testGetPlaceStar() {
        // given
        User user = new User("test@email.com", "encodedPassword", "testUser");

        // Place 객체 생성: 각 Place는 동 이름을 가지고 있음
        Place place1 = new Place("중구","동동");
        Place place2 = new Place("동구","동동동");

        // PlaceStar.of() 정적 팩토리 메서드를 통해 PlaceStar 객체 생성
        PlaceStar star1 = PlaceStar.of(user, place1);
        PlaceStar star2 = PlaceStar.of(user, place2);

        when(userService.findUser()).thenReturn(user);
        when(placeStarRepository.findByUser(user)).thenReturn(Arrays.asList(star1, star2));

        // when
        List<String> result = placeStarService.getPlaceStar();

        // then: 반환된 리스트에 두 개의 동 이름("중구", "동구")가 포함되어 있어야 함
        assertEquals(2, result.size());
        assertTrue(result.contains("동동동"));
        assertTrue(result.contains("동동"));
    }

    @Test
    public void testDelPlaceStar() {
        // given
        String dong = "동동동";

        User user = new User("test@email.com", "encodedPassword", "testUser");
        Place place = new Place("동구","동동동");
        PlaceStar placeStar = PlaceStar.of(user, place);

        when(userService.findUser()).thenReturn(user);
        when(placeService.findPlace("동동동")).thenReturn(place);
        when(placeStarRepository.findByUserAndPlace(user, place)).thenReturn(Optional.of(placeStar));

        // when
        placeStarService.delPlaceStar(dong);

        // then: repository.delete()가 호출되어야 함
        verify(placeStarRepository, times(1)).delete(placeStar);
    }
}
