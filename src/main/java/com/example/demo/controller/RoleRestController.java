package com.example.demo.controller;


import com.example.demo.dto.RoleResponse;
import com.example.demo.repository.RoleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
public class RoleRestController {

    private final RoleRepository roleRepository;

public RoleRestController(RoleRepository roleRepository){
    this.roleRepository = roleRepository;
}

@GetMapping
     public List<RoleResponse> getRoles() {
    return roleRepository.findAll().stream()
            .map(RoleResponse ::from)
            .toList();
}
}
