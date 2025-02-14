package project.mapjiri.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailVerifyRequestDto {
    @NotBlank
    @Email
    private String mail;

    @NotBlank
    private String code;
}
