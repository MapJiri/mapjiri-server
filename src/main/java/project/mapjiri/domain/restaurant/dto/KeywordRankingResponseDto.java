package project.mapjiri.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KeywordRankingResponseDto {
    private final String keyword;
    private final Integer rank;

    @Builder
    private KeywordRankingResponseDto(String keyword, Integer rank) {
        this.keyword = keyword;
        this.rank = rank;
    }

    public static KeywordRankingResponseDto of(String keyword, Integer rank) {
        return KeywordRankingResponseDto.builder()
                .keyword(keyword)
                .rank(rank)
                .build();
    }
}
