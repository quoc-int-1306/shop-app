package com.project.shopapp.Controller;

import com.project.shopapp.Dtos.UpdateUserDTO;
import com.project.shopapp.Dtos.UserDTO;
import com.project.shopapp.Dtos.UserLoginDTO;
import com.project.shopapp.Mapper.ObjectMapper;
import com.project.shopapp.Models.User;
import com.project.shopapp.Responses.LoginResponse;
import com.project.shopapp.Responses.RegisterResponse;
import com.project.shopapp.Responses.UserResponse;
import com.project.shopapp.Service.impl.IUserService;
import com.project.shopapp.Utils.LocalizationUtils;
import com.project.shopapp.Utils.MessageKey;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRePassword())) {
                return ResponseEntity.badRequest().body(localizationUtils.getLocalizedMessage(MessageKey.PASSWORD_NOT_MATCH));
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(RegisterResponse.builder()
                    .message(MessageKey.REGISTER_SUCCESSFULLY)
                    .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
            ) throws Exception {

        try {
            String token = userService
                    .login(userLoginDTO.getPhoneNumber()
                            , userLoginDTO.getPassword(),
                            userLoginDTO.getRoleId()
                            );
            // Trả về token trong response
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY))
                    .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_FAILED, e.getMessage()))
                    .build());
        }
    }
    @PostMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.substring(7);
            User user = userService.getUserDetailFromToken(extractedToken);
            return ResponseEntity.ok().body(ObjectMapper.fromUserToUserResponse(user));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponse> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updateUserDTO,
            @RequestHeader("Authorization") String authorization
    ) throws Exception {
       try {
           System.out.println(updateUserDTO.toString());
           String extractedToken = authorization.substring(7);
           User user = userService.getUserDetailFromToken(extractedToken);

           if(user.getId() != userId) {
               return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
           }

           User updateUser = userService.updateUser(userId, updateUserDTO);
           return ResponseEntity.ok(ObjectMapper.fromUserToUserResponse(updateUser));
       }catch (Exception e) {
           return ResponseEntity.badRequest().build();
       }
    }


}
