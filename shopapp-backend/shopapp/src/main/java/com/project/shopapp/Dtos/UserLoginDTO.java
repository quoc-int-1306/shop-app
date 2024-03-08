package com.project.shopapp.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @JsonProperty("role_id")
    private Long roleId;
}
