package project.mapjiri.domain.review.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ReviewSort {
    LATEST("최신순", "date", Sort.Direction.DESC),
    OLDEST("오래된순", "date", Sort.Direction.ASC),
    HIGH_SCORE("별점높은순", "reviewPoint", Sort.Direction.DESC),
    LOW_SCORE("별점낮은순", "reviewPoint", Sort.Direction.ASC);

    private final String description;
    private final String field;  // JPA에서 정렬할 필드명
    private final Sort.Direction direction;  // 정렬 방식


    // 문자열을 Enum으로 변환
    public static ReviewSort fromString(String sort) {
        return Arrays.stream(values())
                .filter(s -> s.name().equalsIgnoreCase(sort))
                .findFirst()
                .orElse(LATEST); // 기본값: 최신순 정렬
    }
}
