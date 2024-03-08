package com.project.shopapp.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.aspectj.bridge.IMessage;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO {

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @JsonProperty("re_password")
    private String rePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private Integer facebookAccountId;

    @JsonProperty("google_acoount_id")
    private Integer googleAccountId;

    @JsonProperty("role_id")
    private Long roleId;

}
