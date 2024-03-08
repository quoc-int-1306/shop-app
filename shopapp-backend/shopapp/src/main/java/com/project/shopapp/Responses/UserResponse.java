package com.project.shopapp.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.Models.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {
    private Long id;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private Integer facebookAccountId;

    @JsonProperty("google_account_id")
    private Integer googleAccountId;

    private Role role;
}
