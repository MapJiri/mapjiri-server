package project.mapjiri.domain.review.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ReviewTag {
    TASTE("맛"),
    VALUE_FOR_MONEY("가성비"),
    FRIENDLY("친절"),
    AMBIENCE("분위기"),
    PARKING("주차"),
    UNKNOWN("알수없음")
    ;

    private final String description;

    public static ReviewTag fromString(String reviewTag) {
        return Arrays.stream(values())
                .filter(s -> s.description.equals(reviewTag))
                .findFirst()
                .orElse(UNKNOWN); // 기본값: 최신순 정렬
    }
}
