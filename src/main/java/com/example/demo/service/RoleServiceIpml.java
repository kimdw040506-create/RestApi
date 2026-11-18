package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceIpml implements RoleService {

    public RoleRepository roleRepository;

    public RoleServiceIpml(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
