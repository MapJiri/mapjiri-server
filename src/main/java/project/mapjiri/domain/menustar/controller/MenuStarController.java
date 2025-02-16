package project.mapjiri.domain.menustar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mapjiri.domain.menustar.dto.request.AddMenuStarRequest;
import project.mapjiri.domain.menustar.service.MenuStarService;
import project.mapjiri.global.dto.ResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/star/menu")
@RequiredArgsConstructor
public class MenuStarController {

    private final MenuStarService menuStarService;

    @PostMapping
    ResponseEntity<ResponseDto<Void>> addMenuStar(@RequestBody AddMenuStarRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.of(null,"즐겨찾기 추가 성공"));
    }

    @GetMapping
    ResponseEntity<ResponseDto<List<String>>> getMenuStar(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(menuStarService.getMenuStar(),"즐겨찾기 목록 조회 성공"));
    }

    @DeleteMapping
    ResponseEntity<ResponseDto<Void>> delMenuStar(@RequestParam("menuKeyword") String menuKeyword){
        menuStarService.delMenuStar(menuKeyword);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.of(null,"즐겨찾기 삭제 성공"));
    }

}
