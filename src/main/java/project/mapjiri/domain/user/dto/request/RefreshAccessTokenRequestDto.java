package project.mapjiri.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshAccessTokenRequestDto {
    @NotBlank
    private String refreshToken;
}
