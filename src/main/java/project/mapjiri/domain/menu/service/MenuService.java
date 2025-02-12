package project.mapjiri.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.menu.repository.MenuRepository;
import project.mapjiri.global.dto.ResponseDto;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public ResponseDto<List<String>> getMenuType(){
        List<String> menuTypeList = menuRepository.findDistinctType();
        if (menuTypeList.isEmpty()) {
            throw new MyException(MyErrorCode.NOT_FOUND_MENU);
        }
        return ResponseDto.of(menuTypeList, "메뉴 타입 목록 반환 성공");
    }

    public ResponseDto<List<String>> getMenuName(String menuType){
        List<String> menuNameList = menuRepository.findNameByType(menuType);
        if (menuNameList.isEmpty()) {
            throw new MyException(MyErrorCode.NOT_FOUND_MENU);
        }
        return ResponseDto.of(menuNameList, "메뉴 목록 반환 성공");
    }
}
