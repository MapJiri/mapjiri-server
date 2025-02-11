package project.mapjiri.domain.menustar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.menu.model.Menu;
import project.mapjiri.domain.menustar.dto.MenuStarRequest;
import project.mapjiri.domain.menustar.model.MenuStar;
import project.mapjiri.domain.menustar.repository.MenuStarRepository;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.global.dto.ResponseDto;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuStarService {

    private final UserSerivce userService;
    private final MenuService menuService;
    private MenuStarRepository menuStarRepository;

    public ResponseDto<Long> addMenuStar(MenuStarRequest request){

        User user = userService.findUser();
        Menu menu = menuService.findMenu(request.getMenuKeyword());

        if (menuStarRepository.existsByUserAndMenu(user, menu)) {
            throw new MyException(MyErrorCode.ALREADY_FAVORITE_MENU);
        }

        MenuStar menuStar = MenuStar.of(user, menu);
        menuStarRepository.save(menuStar);

        return ResponseDto.of(menu.getMenuId(), "메뉴 즐겨찾기 추가 성공");
    }

    public ResponseDto<List<MenuStar>> getMenuStar(){

        User user = userService.findUser();

        List<MenuStar> menuStarList = menuStarRepository.findByUser(user);

        return ResponseDto.of(menuStarList,"메뉴 즐겨찾기 조회 성공");
    }

    // 즐겨찾기 취소 기능
    public ResponseDto<Void> delMenuStar(String menuKeyword) {

        User user = userService.findUser();
        Menu menu = menuService.findMenu(menuKeyword);

        MenuStar menuStar = menuStarRepository.findByUserAndMenu(user, menu)
                .orElseThrow(() -> new MyException(MyErrorCode.NOT_FOUND_FAVORITE_MENU));
        menuStarRepository.delete(menuStar);

        return ResponseDto.of(null, "즐겨찾기 취소 성공");
    }
}
