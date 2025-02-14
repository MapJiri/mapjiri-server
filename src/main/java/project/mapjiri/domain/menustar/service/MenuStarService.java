package project.mapjiri.domain.menustar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.menu.model.Menu;
import project.mapjiri.domain.menu.service.MenuService;
import project.mapjiri.domain.menustar.dto.MenuStarRequest;
import project.mapjiri.domain.menustar.model.MenuStar;
import project.mapjiri.domain.menustar.repository.MenuStarRepository;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.domain.user.service.UserService;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuStarService {

    private final UserService userService;
    private final MenuService menuService;
    private final MenuStarRepository menuStarRepository;

    public MenuStar addMenuStar(MenuStarRequest request){

        User user = userService.findUser();
        Menu menu = menuService.findMenu(request.getMenuKeyword());

        if (menuStarRepository.existsByUserAndMenu(user, menu)) {
            throw new MyException(MyErrorCode.ALREADY_FAVORITE_MENU);
        }

        MenuStar menuStar = MenuStar.of(user, menu);
        menuStarRepository.save(menuStar);

        return menuStar;
    }

    public List<String> getMenuStar(){

        User user = userService.findUser();
        List<MenuStar> menuStarList = menuStarRepository.findByUser(user);

        return menuStarList.stream()
                .map(menuStar -> menuStar.getMenu().getMenuName()) // 메뉴 이름만 리스트로 변환
                .collect(Collectors.toList());
    }

    // 즐겨찾기 취소 기능
    public void delMenuStar(String menuKeyword) {

        User user = userService.findUser();
        Menu menu = menuService.findMenu(menuKeyword);

        MenuStar menuStar = menuStarRepository.findByUserAndMenu(user, menu)
                .orElseThrow(() -> new MyException(MyErrorCode.NOT_FOUND_FAVORITE_MENU));
        menuStarRepository.delete(menuStar);
    }
}
