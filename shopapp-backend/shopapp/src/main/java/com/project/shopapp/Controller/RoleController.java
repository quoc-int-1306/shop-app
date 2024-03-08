package com.project.shopapp.Controller;

import com.project.shopapp.Models.Role;
import com.project.shopapp.Service.impl.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = roleService.getAllRole();
        return ResponseEntity.ok(roles);
    }
}
