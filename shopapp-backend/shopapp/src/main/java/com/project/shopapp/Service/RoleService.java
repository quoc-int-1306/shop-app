package com.project.shopapp.Service;

import com.project.shopapp.Models.Role;
import com.project.shopapp.Repository.RoleRepository;
import com.project.shopapp.Service.impl.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
