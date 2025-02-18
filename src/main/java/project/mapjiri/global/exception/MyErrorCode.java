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
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"등록된 사용자가 아닙니다." ),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 번호가 일치하지 않습니다."),
    EXPIRED_OR_NOT_FOUND_VERIFICATION_CODE(HttpStatus.NOT_FOUND, "인증 번호가 만료되었거나 존재하지 않습니다."),
    EMAIL_VERIFICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "이메일 인증이 필요합니다."),
    ALREADY_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND,"존재하지 않는 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token 입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "이미 만료된 Refresh Token 입니다."),
    UNAUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "인증된 사용자가 없습니다."),
    NOT_FOUNT_RESTAURANT(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다.");

    private final HttpStatus status;
    private final String message;
}

