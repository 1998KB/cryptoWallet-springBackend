package com.wallet.wallet.controller;
import com.wallet.wallet.model.UserObj;
import com.wallet.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;


@RestController
public class UserController {

    private final UserService userService;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, InMemoryUserDetailsManager inMemoryUserDetailsManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/newUser")
    public ResponseEntity<String> createUser(@RequestBody UserObj user) {
        try {
            userService.createUser(user.getUsername(), user.getEmail(), user.getAge(), user.getPassword());
            inMemoryUserDetailsManager.createUser(
                    User.withUsername(user.getUsername())
                            .password(passwordEncoder.encode(user.getPassword()))
                            .roles("USER")
                            .build()
            );
            return new ResponseEntity<>("User created", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserObj> getUserById(@PathVariable Long id) {
        if (userService.getUserById(id)!= null) {
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("This user do not exist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<UserObj> updateUser(@PathVariable Long id, @RequestBody UserObj updatedUser) {
        try {
            UserObj user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
