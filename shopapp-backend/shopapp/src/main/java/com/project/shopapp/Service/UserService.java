package com.project.shopapp.Service;

import com.project.shopapp.Components.JwtTokenUtil;
import com.project.shopapp.Dtos.UpdateUserDTO;
import com.project.shopapp.Dtos.UserDTO;
import com.project.shopapp.Exceptions.DataNotFound;
import com.project.shopapp.Models.Role;
import com.project.shopapp.Models.User;
import com.project.shopapp.Repository.RoleRepository;
import com.project.shopapp.Repository.UserRepository;
import com.project.shopapp.Service.impl.IUserService;
import com.project.shopapp.Utils.LocalizationUtils;
import com.project.shopapp.Utils.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LocalizationUtils localizationUtils;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFound {
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiểm tra xem số điện thoại tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .active(true)
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .dateOfBirth(userDTO.getDateOfBirth())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFound("Role not found"));
        newUser.setRole(role);
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getFacebookAccountId() == 0) {
            String password = userDTO.getPassword();
            String endcodePassword = passwordEncoder.encode(password);
            newUser.setPassword(endcodePassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()) {
            throw new DataNotFound("Invalid phonenumber or password");
        }
        User existingUser = optionalUser.get();

        if(existingUser.getFacebookAccountId() == 0 && existingUser.getFacebookAccountId() == 0) {
            if(!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong phonenumber or password");
            }
        }

        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())) {
            throw new DataNotFound(localizationUtils.getLocalizedMessage(MessageKey.ROLE_NOT_EXIST));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, existingUser.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(optionalUser.get());
    }

    @Override
    public User getUserDetailFromToken(String token) throws Exception {
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if(user.isPresent()) {
            return user.get();
        }else {
            throw new Exception("User not found");
        }
    }

    @Override
    @Transactional
    public User updateUser(Long userId, UpdateUserDTO userDTO) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFound("User not found"));

        if(userDTO.getFullName() != null) {
            existingUser.setFullName(userDTO.getFullName());
        }
        if(userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }

        if(userDTO.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(userDTO.getDateOfBirth());
        }

        if(userDTO.getPassword() != null &&
            !userDTO.getPassword().isEmpty()
        ) {
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                throw new DataNotFound("Password and retypepassword not the same");
            }
            System.out.println("Update mat khau" + userDTO.getPassword());
            String newPassword = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodePassword);
        }
        System.out.println("Update thanh cong");
        return userRepository.save(existingUser);

    }
}
