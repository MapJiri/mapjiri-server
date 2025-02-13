package project.mapjiri.domain.restaurant.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import project.mapjiri.domain.restaurant.dto.SearchRankingResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private SearchService searchService;

    private static final String REDIS_SEARCH_KEY_PREFIX = "search:";
    private static final String REDIS_SEARCH_RANKING_KEY = "ranking";
    private static final DateTimeFormatter REDIS_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");


    @Test
    void 검색_키워드_카운트를_증가_시킨다() {
        //given
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        String currentKey = REDIS_SEARCH_KEY_PREFIX + LocalDateTime.now().format(REDIS_TIME_FORMATTER);
        String testKeyword = "돈까스";

        //when
        searchService.incrementSearchCount(testKeyword);

        //then
        verify(zSetOperations, times(1)).incrementScore(currentKey, testKeyword, 1);
        verify(redisTemplate, times(1)).expire(eq(currentKey), anyLong(), any());
    }

    @Test
    void 캐시된_랭킹을_조회한다() {
        // given
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        Set<ZSetOperations.TypedTuple<String>> mockRankingSet = new LinkedHashSet<>();
        mockRankingSet.add(new MockTypedTuple<>("치킨", 1021.0));
        mockRankingSet.add(new MockTypedTuple<>("햄버거", 866.0));
        mockRankingSet.add(new MockTypedTuple<>("피자", 661.0));
        mockRankingSet.add(new MockTypedTuple<>("짜장면", 479.0));
        mockRankingSet.add(new MockTypedTuple<>("떡볶이", 333.0));
        mockRankingSet.add(new MockTypedTuple<>("마라탕", 199.0));
        mockRankingSet.add(new MockTypedTuple<>("돈까스", 187.0));
        mockRankingSet.add(new MockTypedTuple<>("족발", 132.0));
        mockRankingSet.add(new MockTypedTuple<>("파스타", 97.0));
        mockRankingSet.add(new MockTypedTuple<>("냉면", 89.0));

        when(zSetOperations.reverseRangeWithScores(eq(REDIS_SEARCH_KEY_PREFIX + REDIS_SEARCH_RANKING_KEY), eq(0L), eq(9L)))
                .thenReturn(mockRankingSet);

        //when
        List<SearchRankingResponseDto> searchRankingResponseDtos = searchService.getRankings();

        //then
        assertAll(
                () -> assertThat(searchRankingResponseDtos.get(0).getRanking()).isEqualTo(1),
                () -> assertThat(searchRankingResponseDtos.get(0).getKeyword()).isEqualTo("치킨"),
                () -> assertThat(searchRankingResponseDtos.get(1).getRanking()).isEqualTo(2),
                () -> assertThat(searchRankingResponseDtos.get(1).getKeyword()).isEqualTo("햄버거"),
                () -> assertThat(searchRankingResponseDtos.get(2).getRanking()).isEqualTo(3),
                () -> assertThat(searchRankingResponseDtos.get(2).getKeyword()).isEqualTo("피자"),
                () -> assertThat(searchRankingResponseDtos.get(3).getRanking()).isEqualTo(4),
                () -> assertThat(searchRankingResponseDtos.get(3).getKeyword()).isEqualTo("짜장면"),
                () -> assertThat(searchRankingResponseDtos.get(4).getRanking()).isEqualTo(5),
                () -> assertThat(searchRankingResponseDtos.get(4).getKeyword()).isEqualTo("떡볶이"),
                () -> assertThat(searchRankingResponseDtos.get(5).getRanking()).isEqualTo(6),
                () -> assertThat(searchRankingResponseDtos.get(5).getKeyword()).isEqualTo("마라탕"),
                () -> assertThat(searchRankingResponseDtos.get(6).getRanking()).isEqualTo(7),
                () -> assertThat(searchRankingResponseDtos.get(6).getKeyword()).isEqualTo("돈까스"),
                () -> assertThat(searchRankingResponseDtos.get(7).getRanking()).isEqualTo(8),
                () -> assertThat(searchRankingResponseDtos.get(7).getKeyword()).isEqualTo("족발"),
                () -> assertThat(searchRankingResponseDtos.get(8).getRanking()).isEqualTo(9),
                () -> assertThat(searchRankingResponseDtos.get(8).getKeyword()).isEqualTo("파스타"),
                () -> assertThat(searchRankingResponseDtos.get(9).getRanking()).isEqualTo(10),
                () -> assertThat(searchRankingResponseDtos.get(9).getKeyword()).isEqualTo("냉면")
        );

        verify(zSetOperations, times(1)).reverseRangeWithScores(anyString(), anyLong(), anyLong());
        verify(redisTemplate, never()).expire(anyString(), anyLong(), any());
    }

    @Test
    void 캐시된_랭킹이_없다면_새로운_랭킹을_저장한다() {
        //given
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.reverseRangeWithScores(anyString(), anyLong(), anyLong())).thenReturn(Collections.emptySet());

        //when
        searchService.getRankings();

        //then
        verify(zSetOperations, times(1)).unionAndStore(eq(null), anyList(), eq(REDIS_SEARCH_KEY_PREFIX + REDIS_SEARCH_RANKING_KEY));
        verify(redisTemplate, times(1)).expire(anyString(), anyLong(), any());
    }

    private static class MockTypedTuple<V> implements ZSetOperations.TypedTuple<V> {
        private final V value;
        private final Double score;

        public MockTypedTuple(V value, Double score) {
            this.value = value;
            this.score = score;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public Double getScore() {
            return score;
        }

        @Override
        public int compareTo(ZSetOperations.TypedTuple<V> o) {
            return Double.compare(this.getScore(), o.getScore());
        }
    }
}
