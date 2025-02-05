package project.mapjiri.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MyErrorCode {
    // 예시
    COURSE_CANNOT_BE_DELETED(HttpStatus.BAD_REQUEST, "수강생이 있어 강좌를 삭제할 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}

