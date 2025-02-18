package project.mapjiri.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDto {
    @NotBlank
    private String refreshToken;

    private String accessToken;

    private String kakaoAccessToken;

    // accessToken만 받는 생성자
    public LogoutRequestDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
