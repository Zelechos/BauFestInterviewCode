package com.code.challenge.user_engine.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    private String name;

    @NotBlank
    @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 12)
    @Pattern(regexp = "^(?=(.*[A-Z]){1})(?=(.*\\d){2})[a-zA-Z\\d]+$")
    private String password;

    private List<PhoneDto> phones;
}
