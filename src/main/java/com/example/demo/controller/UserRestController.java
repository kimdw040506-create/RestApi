    package com.example.demo.controller;

    import com.example.demo.dto.UserRequest;
    import com.example.demo.model.User;
    import com.example.demo.service.UserService;
    import jakarta.validation.Valid;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import com.example.demo.dto.UserResponse;
    import com.example.demo.exception.UserNotFoundException;

    import java.net.URI;
    import java.util.List;

    @RestController
    @RequestMapping("/api/admin/users")
    public class UserRestController {

        private final UserService userService;

        public UserRestController(UserService userService) {
            this.userService = userService;
        }

        @GetMapping
        public List<UserResponse> getAllUsers() {
            return userService.findAll().stream()
                    .map(UserResponse::from)
                    .toList();
        }

        @GetMapping("/{id}")
        public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
            User user = userService.findById(id);
            if (user == null) {
                throw new UserNotFoundException(id);
            }
            return ResponseEntity.ok(UserResponse.from(user));
        }

        @PostMapping
        public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequest request) {
            User saved = userService.create(request);

            return ResponseEntity
                    .created(URI.create("/api/admin/users/" + saved.getId()))
                    .body(UserResponse.from(saved));
        }

        @PutMapping("/{id}")
        public ResponseEntity<UserResponse> updateUser(
                @PathVariable Long id,
                @Valid @RequestBody UserRequest request) {
            User updated = userService.update(id, request);

            if(updated == null){
                throw new UserNotFoundException(id);
            }

            return ResponseEntity.ok(UserResponse.from(updated));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity <Void> deleteUser(@PathVariable Long id){
            if (userService.findById(id) == null) {
                throw new UserNotFoundException(id);
            }
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
    }