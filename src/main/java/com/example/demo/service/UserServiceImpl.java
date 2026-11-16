package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll()  {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    private Set<Role> findRolesByIds(Set<Long> roleIds) {
        return roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Role not found: " + roleId)))
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public User create(UserRequest request) {
        if(userRepository.findByUsername(request.username()).isPresent()){
            throw new IllegalArgumentException("Username already exists");
        }
        if(request.password() == null || request.password().isBlank()){
            throw new IllegalArgumentException("Password must not be blank");
        }
        if(request.roleIds() == null || request.roleIds().isEmpty()){
            throw new IllegalArgumentException("At least one role is required");
        }

        User user = new User();
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(findRolesByIds(request.roleIds()));

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(long id, UserRequest request) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return null;
        }

        User userWithSameUsername = userRepository
                .findByUsername(request.username())
                .orElse(null);

        if (userWithSameUsername != null
                && !userWithSameUsername.getId().equals(existingUser.getId())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (request.roleIds() == null || request.roleIds().isEmpty()) {
            throw new IllegalArgumentException("At least one role is required");
        }

        existingUser.setName(request.name());
        existingUser.setSurname(request.surname());
        existingUser.setEmail(request.email());
        existingUser.setUsername(request.username());
        existingUser.setRoles(findRolesByIds(request.roleIds()));

        if (request.password() != null && !request.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.password()));
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}