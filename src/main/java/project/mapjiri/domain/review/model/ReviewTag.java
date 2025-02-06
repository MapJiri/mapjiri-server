package project.mapjiri.domain.review.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum ReviewTag {
    TASTE("맛"),
    VALUE_FOR_MONEY("가성비"),
    FRIENDLY("친절"),
    AMBIENCE("분위기"),
    PARKING("주차"),
    UNKNOWN("알수없음"),
    NO_TAG("없음"),
    ;

    private final String description;

    private static final int MIN_COUNT_THRESHOLD = 20;

    public static ReviewTag fromString(String reviewTag) {
        return Arrays.stream(values())
                .filter(tag -> tag.description.equals(reviewTag))
                .findFirst()
                .orElse(UNKNOWN); // 기본값: 최신순 정렬
    }

    public static ReviewTag findMostFrequentTag(String[] reviewTags) {
        Map<ReviewTag, Integer> tagCountMap = countTagFrom(reviewTags);

        return getTopReviewTag(tagCountMap);
    }

    private static Map<ReviewTag, Integer> countTagFrom(String[] reviewTags) {
        Map<ReviewTag, Integer> tagCountMap = new HashMap<>();

        for (String reviewTag : reviewTags) {
            ReviewTag tag = ReviewTag.fromString(reviewTag);
            tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
        }
        return tagCountMap;
    }

    private static ReviewTag getTopReviewTag(Map<ReviewTag, Integer> tagCountMap) {
        ReviewTag mostFrequentTag = ReviewTag.UNKNOWN;
        int maxCount = 0;
        for (Map.Entry<ReviewTag, Integer> entry : tagCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentTag = entry.getKey();
            }
        }

        return MIN_COUNT_THRESHOLD > maxCount ? NO_TAG : mostFrequentTag;
    }
}
