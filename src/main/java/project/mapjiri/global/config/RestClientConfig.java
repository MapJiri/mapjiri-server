package project.mapjiri.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.springframework.http.HttpHeaders.*;

@Configuration
public class RestClientConfig {

    private final static String KAKAO_BASE_URI = "https://dapi.kakao.com/v2/local";

    @Bean
    public RestClient kakaoRestClient(@Value("${kakao.api.key}") String apiKey) {
        return RestClient.builder()
                .uriBuilderFactory(createUriBuilderFactory(KAKAO_BASE_URI))
                .defaultHeaders(headers -> headers.set(AUTHORIZATION, "KakaoAK " + apiKey))
                .build();
    }

    private DefaultUriBuilderFactory createUriBuilderFactory(String uri) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(uri);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return factory;
    }
}
