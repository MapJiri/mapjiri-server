package project.mapjiri.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchRankingResponseDto {
    private final Integer ranking;
    private final String keyword;

    @Builder
    private SearchRankingResponseDto(Integer ranking, String keyword) {
        this.ranking = ranking;
        this.keyword = keyword;
    }

    public static SearchRankingResponseDto of(int ranking, String keyword) {
        return SearchRankingResponseDto.builder()
                .ranking(ranking)
                .keyword(keyword)
                .build();
    }
}
