package project.mapjiri.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.menu.model.Menu;
import project.mapjiri.domain.menu.repository.MenuRepository;
import project.mapjiri.global.dto.ResponseDto;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<String> getMenuType(){
        List<String> menuTypeList = menuRepository.findDistinctType();
        if (menuTypeList.isEmpty()) {
            throw new MyException(MyErrorCode.NOT_FOUND_MENU);
        }
        return menuTypeList;
    }

    public List<String> getMenuName(String menuType){
        List<String> menuNameList = menuRepository.findNameByType(menuType);
        if (menuNameList.isEmpty()) {
            throw new MyException(MyErrorCode.NOT_FOUND_MENU);
        }
        return menuNameList;
    }

    public Menu findMenu(String menuName){
        return menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MyException(MyErrorCode.NOT_FOUND_MENU));
    }
}
