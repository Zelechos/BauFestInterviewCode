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
    @Pattern(regexp = "^[A-Za-z]{7}@[A-Za-z0-9.-]{3,30}\\.[A-Za-z]{2,10}$", message = "El correo debe tener exactamente 7 letras en el usuario y un formato válido")
    private String email;

    @NotBlank
    @Size(min = 8, max = 12)
    @Pattern(regexp = "^(?=[^A-Z]*[A-Z][^A-Z]*$)(?=(?:[^0-9]*[0-9]){2}[^0-9]*$)[a-zA-Z\\d]{8,12}$", message = "La clave debe tener solo una Mayúscula y solamente dos números, minúsculas, largo máximo de 12 y mínimo 8")
    private String password;

    private List<PhoneDto> phones;
}
