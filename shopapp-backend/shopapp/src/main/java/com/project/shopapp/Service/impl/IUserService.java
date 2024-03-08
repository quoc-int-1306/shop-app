package com.project.shopapp.Service.impl;

import com.project.shopapp.Dtos.UpdateUserDTO;
import com.project.shopapp.Dtos.UserDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Models.User;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {

    User createUser(UserDTO userDTO) throws DataNotFound;

    String login(String phoneNumber, String password, Long roleId) throws Exception;

    User getUserDetailFromToken(String token) throws Exception;

    User updateUser(Long userId, UpdateUserDTO userDTO) throws Exception;
}
