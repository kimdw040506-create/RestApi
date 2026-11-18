package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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

    private void validateUsername(String username, Long currentUserId) {
        User userWithSameUsername = userRepository.findByUsername(username).orElse(null);
        if (userWithSameUsername != null
                && !userWithSameUsername.getId().equals(currentUserId)) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    private void validateRoles(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one role is required");
        }
    }

    private void fillUsersFields(User user, UserRequest userRequest) {
        user.setName(userRequest.name());
        user.setSurname(userRequest.surname());
        user.setEmail(userRequest.email());
        user.setUsername(userRequest.username());
        user.setRoles(findRolesByIds(userRequest.roleIds()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
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
        validateUsername(request.username(), null);
        validateRoles(request.roleIds());

        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }

        User user = new User();
        fillUsersFields(user, request);
        user.setPassword(passwordEncoder.encode(request.password()));

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(long id, UserRequest request) {
        User existingUser = findById(id);

        validateUsername(request.username(), existingUser.getId());
        validateRoles(request.roleIds());
        fillUsersFields(existingUser, request);

        if (request.password() != null && !request.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.password()));
        }
        return userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.delete(findById(id));
    }
}