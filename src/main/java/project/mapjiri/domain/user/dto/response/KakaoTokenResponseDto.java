package project.mapjiri.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoTokenResponseDto {

    @JsonProperty("access_token")
    private String accessToken;
}
