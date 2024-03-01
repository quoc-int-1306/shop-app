package com.project.shopapp.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.Models.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}
