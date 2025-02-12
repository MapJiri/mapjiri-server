package project.mapjiri.domain.user.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 영어 대문자, 숫자, 특수문자(@$!%*?&)를 포함해야 합니다."
    )
    private String password;

    @NotBlank
    @Size(min = 2, max = 10)
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]$",
            message = "닉네임은 한글, 영문, 숫자만 사용할 수 있습니다."
    )
    private String username;
}
