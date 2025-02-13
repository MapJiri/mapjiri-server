package project.mapjiri.domain.restaurant.service;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import project.mapjiri.domain.restaurant.dto.SearchRankingResponseDto;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int RANKING_LIMIT = 10;
    private static final String REDIS_SEARCH_KEY_PREFIX = "search:";
    private static final String REDIS_SEARCH_RANKING_KEY = "ranking";
    private static final DateTimeFormatter REDIS_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final int REDIS_SEARCH_EXPIRATION_SECONDS = 600;
    private static final int REDIS_RANKING_EXPIRATION_SECONDS = 60;


    public void incrementSearchCount(String keyword) {
        String currentKey = REDIS_SEARCH_KEY_PREFIX + LocalDateTime.now().format(REDIS_TIME_FORMATTER);
        redisTemplate.opsForZSet().incrementScore(currentKey, keyword, 1);
        redisTemplate.expire(currentKey, REDIS_SEARCH_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public List<SearchRankingResponseDto> getRankings() {
        Set<ZSetOperations.TypedTuple<String>> cachedRankings = getCachedRanking();
        if (!cachedRankings.isEmpty()) {
            return convertRankingToList(cachedRankings);
        }
        generateRanking();
        Set<ZSetOperations.TypedTuple<String>> newRankings = getCachedRanking();
        return convertRankingToList(newRankings);
    }


    private void generateRanking() {
        LocalDateTime now = LocalDateTime.now();
        List<String> recentKeys = new ArrayList<>();
        for (int minute = 0; minute < RANKING_LIMIT; minute++) {
            recentKeys.add(REDIS_SEARCH_KEY_PREFIX + now.minusMinutes(minute).format(REDIS_TIME_FORMATTER));
        }
        String newRankingKey = REDIS_SEARCH_KEY_PREFIX + REDIS_SEARCH_RANKING_KEY;
        redisTemplate.opsForZSet().unionAndStore(null, recentKeys, newRankingKey);
        redisTemplate.expire(newRankingKey, REDIS_RANKING_EXPIRATION_SECONDS, TimeUnit.SECONDS);

    }

    private List<SearchRankingResponseDto> convertRankingToList(Set<ZSetOperations.TypedTuple<String>> rankedSearches) {
        List<SearchRankingResponseDto> result = new ArrayList<>(RANKING_LIMIT);
        int ranking = 1;
        for (ZSetOperations.TypedTuple<String> tuple : rankedSearches) {
                result.add(SearchRankingResponseDto.of(ranking++, tuple.getValue()));
        }
        return result;
    }

    private Set<ZSetOperations.TypedTuple<String>> getCachedRanking() {
        return Optional.ofNullable(redisTemplate.opsForZSet()
                        .reverseRangeWithScores(REDIS_SEARCH_KEY_PREFIX + REDIS_SEARCH_RANKING_KEY, 0, RANKING_LIMIT - 1))
                .orElse(Collections.emptySet());
    }
}