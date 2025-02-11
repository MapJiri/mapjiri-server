package project.mapjiri.domain.menustar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mapjiri.domain.menustar.dto.MenuStarRequest;
import project.mapjiri.domain.menustar.model.MenuStar;
import project.mapjiri.domain.menustar.service.MenuStarService;
import project.mapjiri.global.dto.ResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/star/menu")
@RequiredArgsConstructor
public class MenuStarController {

    private final MenuStarService menuStarService;

    @PostMapping
    ResponseEntity<ResponseDto<Long>> addMenuStar(@RequestBody MenuStarRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(menuStarService.addMenuStar(request));
    }

    @GetMapping
    ResponseEntity<ResponseDto<List<MenuStar>>> getMenuStar(){
        return ResponseEntity.status(HttpStatus.OK).body(menuStarService.getMenuStar());
    }

    @DeleteMapping
    ResponseEntity<ResponseDto<Void>> delMenuStar(@RequestParam("menuKeyword") String menuKeyword){
        return ResponseEntity.status(HttpStatus.OK).body(menuStarService.delMenuStar(menuKeyword));
    }

}
