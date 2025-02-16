package project.mapjiri.domain.menustar.service;

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
import project.mapjiri.domain.menustar.dto.request.AddMenuStarRequest;
import project.mapjiri.domain.menustar.model.MenuStar;
import project.mapjiri.domain.menustar.repository.MenuStarRepository;
import project.mapjiri.domain.menu.model.Menu;
import project.mapjiri.domain.menu.service.MenuService;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class MenuStarServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private MenuService menuService;

    @Mock
    private MenuStarRepository menuStarRepository;

    @InjectMocks
    private MenuStarService menuStarService;

    @Test
    public void testAddMenuStar() {
        // given
        AddMenuStarRequest request = new AddMenuStarRequest();
        request.setMenuKeyword("치킨");

        User user = new User("test@email.com", "encodedPassword", "testUser");
        Menu menu = new Menu("한식", "치킨");

        when(userService.findUser()).thenReturn(user);
        when(menuService.findMenu("치킨")).thenReturn(menu);
        when(menuStarRepository.existsByUserAndMenu(user, menu)).thenReturn(false);

        // when
        menuStarService.addMenuStar(request);

        // then
        verify(menuStarRepository, times(1)).save(any(MenuStar.class));
    }

    @Test
    public void testGetMenuStar() {
        // given
        User user = new User("test@email.com", "encodedPassword", "testUser");

        Menu menu1 = new Menu("한식", "백반");
        Menu menu2 = new Menu("한식", "찌개");

        MenuStar star1 = MenuStar.of(user, menu1);
        MenuStar star2 = MenuStar.of(user, menu2);

        when(userService.findUser()).thenReturn(user);
        when(menuStarRepository.findByUser(user)).thenReturn(Arrays.asList(star1, star2));

        // when
        List<String> result = menuStarService.getMenuStar();

        // then: 즐겨찾기 메뉴 이름 리스트에 두 메뉴가 포함되어야 함
        assertEquals(2, result.size());
        assertTrue(result.contains("백반"));
        assertTrue(result.contains("찌개"));
    }

    @Test
    public void testDelMenuStar_Success() {
        // given
        String menuKeyword = "찌개";

        User user = new User("test@email.com", "encodedPassword", "testUser");
        Menu menu = new Menu("한식", "찌개");
        MenuStar menuStar = MenuStar.of(user, menu);

        when(userService.findUser()).thenReturn(user);
        when(menuService.findMenu("찌개")).thenReturn(menu);
        when(menuStarRepository.findByUserAndMenu(user, menu)).thenReturn(Optional.of(menuStar));

        // when
        menuStarService.delMenuStar(menuKeyword);

        // then: 해당 MenuStar 객체가 삭제되었는지 검증
        verify(menuStarRepository, times(1)).delete(menuStar);
    }
}
