package project.mapjiri.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakao")
public class KakaoConfig {

    private String clientId;
    private String apiKey;
    private String redirectUri; // ✅ redirect_uri 제거하고, redirect_uris만 사용

}




