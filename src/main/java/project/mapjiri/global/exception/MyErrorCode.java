package project.mapjiri.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MyErrorCode {
    // 예시
    COURSE_CANNOT_BE_DELETED(HttpStatus.BAD_REQUEST, "수강생이 있어 강좌를 삭제할 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "외부 서비스에서 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    ALREADY_FAVORITE_PLACE(HttpStatus.BAD_REQUEST,"이미 즐겨찾기에 등록된 위치 입니다." ),
    NOT_FOUND_FAVORITE_PLACE(HttpStatus.NOT_FOUND,"즐겨찾기에 등록되지 않은 위치 입니다." ),
    ALREADY_FAVORITE_MENU(HttpStatus.BAD_REQUEST, "이미 즐겨찾기에 등록된 메뉴 입니다." ),
    NOT_FOUND_FAVORITE_MENU(HttpStatus.NOT_FOUND,"즐겨찾기에 등록되지 않은 메뉴 입니다." ),
    NOT_FOUND_PLACE(HttpStatus.NOT_FOUND,"등록된 위치가 없습니다."),
    NOT_FOUND_MENU(HttpStatus.NOT_FOUND,"등록된 메뉴가 없습니다."),
    NOT_FOUND_CSVFILE(HttpStatus.NOT_FOUND,"CSV 파일을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}

