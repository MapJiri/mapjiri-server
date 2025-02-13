package project.mapjiri.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDto {
    @NotBlank
    private String refreshToken;

    @NotBlank
    private String accessToken;
}
