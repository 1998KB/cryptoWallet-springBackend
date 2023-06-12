package com.wallet.wallet.controller;

import com.wallet.wallet.model.UserObj;
import com.wallet.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;


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
    public ResponseEntity<UserObj> getUserById(@PathVariable Long id, Principal principal) {
        UserObj user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!isAuthorized(user.getUsername(), principal)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id, Principal principal) {
        UserObj user = userService.getUserById(id);
        try {
            if (!isAuthorized(user.getUsername(), principal)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            userService.deleteUserById(id);
            inMemoryUserDetailsManager.deleteUser(user.getUsername());
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("This user do not exist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<UserObj> updateUser(@PathVariable Long id, @RequestBody UserObj updatedUser, Principal principal) {
        System.out.println("patch user: " + userService.getUserById(id).getUsername() + "principal: " + principal.getName());
        try {
            if (!isAuthorized(userService.getUserById(id).getUsername(), principal)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(userService.getUserById(id).getUsername());
            UserObj user = userService.updateUser(id, updatedUser);
            userDetails = User.withUserDetails(userDetails)
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .build();
            inMemoryUserDetailsManager.deleteUser(userDetails.getUsername());
            inMemoryUserDetailsManager.createUser(userDetails);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isAuthorized(String username, Principal principal) {
        return username.equals(principal.getName()) || principal.getName().equals("admin");
    }

}
