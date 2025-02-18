package project.mapjiri.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.mapjiri.domain.menu.service.MenuService;
import project.mapjiri.global.dto.ResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/type")
    public ResponseEntity<ResponseDto<List<String>>> getMenuType(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(menuService.getMenuType(),"메뉴 타입 반환 성공"));
    }

    @GetMapping("/name")
    public ResponseEntity<ResponseDto<List<String>>> getMenuName(@RequestParam("menuType") String menuType){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(menuService.getMenuName(menuType),"메뉴 목록 반환 성공"));
    }

    @GetMapping("/find-type")
    public ResponseEntity<ResponseDto<String>> findMenuType(@RequestParam("menuName") String menuName){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(menuService.findMenu(menuName).getMenuType(),"메뉴 이름 반환 성공"));
    }
}
