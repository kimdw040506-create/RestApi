    package com.example.demo.controller;

    import com.example.demo.model.User;
    import com.example.demo.repository.UserRepository;
    import com.example.demo.service.UserService;
    import jakarta.validation.Valid;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.*;


    import java.net.URI;
    import java.util.List;

    @RestController
    @RequestMapping("/api/admin/users")
    public class UserRestController {

        private final UserService userService;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public UserRestController(UserService userService,
                                  UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
            this.userService = userService;
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }


        @GetMapping
        public List<User> getAllUsers() {
            return userService.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<User> getById(@PathVariable Long id) {
            User user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        }

        @PostMapping
        public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            if (user.getPassword() == null || user.getPassword().isBlank()) {
                return ResponseEntity.badRequest().build();
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            User saved = userService.save(user);

            return ResponseEntity
                    .created(URI.create("/api/admin/users/" + saved.getId()))
                    .body(saved);
        }


        @PutMapping("/{id}")
        public ResponseEntity<User> updateUser(
                @PathVariable Long id,
                @Valid @RequestBody User user) {
            User existingUser = userService.findById(id);

            if(existingUser == null){
                return ResponseEntity.notFound().build();
            }

            User userWithSameUsername = userRepository
                    .findByUsername(user.getUsername())
                    .orElse(null);
            if (userWithSameUsername != null
                    && !userWithSameUsername.getId().equals(existingUser.getId())) {
                return ResponseEntity.badRequest().build();
            }

            if (user.getPassword()!= null && !user.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setEmail(user.getEmail());
            existingUser.setUsername(user.getUsername());
            existingUser.setRoles(user.getRoles());

            userService.update(existingUser);

            return ResponseEntity.ok(existingUser);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity <Void> deleteUser(@PathVariable Long id){
            if (userService.findById(id) == null) {
                return ResponseEntity.notFound().build();
            }
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
    }