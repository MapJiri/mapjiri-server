package project.mapjiri.domain.menu.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mapjiri.domain.menu.model.Menu;
import project.mapjiri.domain.menu.repository.MenuRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    public void 메뉴_타입_목록_조회() {
        // given
        List<String> menuTypes = Arrays.asList("한식", "중식", "일식");
        when(menuRepository.findDistinctType()).thenReturn(menuTypes);

        // when
        List<String> result = menuService.getMenuType();

        // then
        assertThat(menuTypes).isEqualTo(result);
    }

    @Test
    public void 메뉴_이름_조회() {
        // given
        String menuType = "한식";
        List<String> menuNames = Arrays.asList("치킨", "백반", "찌개");
        when(menuRepository.findNameByType(menuType)).thenReturn(menuNames);

        // when
        List<String> result = menuService.getMenuName(menuType);

        // then
        assertThat(menuNames).isEqualTo(result);
    }

    @Test
    public void 메뉴_찾기() {
        // given
        String menuName = "치킨";
        Menu menu = new Menu("한식", menuName); // 필요한 필드를 설정하세요.
        when(menuRepository.findByMenuName(menuName)).thenReturn(Optional.of(menu));

        // when
        Menu result = menuService.findMenu(menuName);

        // then
        assertThat(menu).isEqualTo(result);
    }
}
