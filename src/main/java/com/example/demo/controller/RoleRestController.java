package com.example.demo.controller;


import com.example.demo.dto.RoleResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
public class RoleRestController {

    private final RoleService roleService;

    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleResponse> getRoles() {
        return roleService.findAll().stream()
                .map(RoleResponse::from)
                .toList();
    }
}
