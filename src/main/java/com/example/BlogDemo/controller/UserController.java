package com.example.BlogDemo.controller;

import com.example.BlogDemo.entities.User;
import com.example.BlogDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user) {
        user = userService.save(user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUser() {
        return getAllUser(0, Integer.MAX_VALUE);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUser(@RequestParam int page, @RequestParam int perPage) {
        Page<User> users = userService.findAllByPage(page, perPage);
        return new ResponseEntity<>(users.getContent(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isPresent()) {
            return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not exist");
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isPresent()) {
            userService.delete(optionalUser.get());
            return ResponseEntity.status(HttpStatus.OK).body("User " + username + " deleted");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not exist");
        }
    }

    @PutMapping("/{username}")
    @PreAuthorize("(authentication.principal.username == #username) || hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, @PathVariable String username) {
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isPresent()) {
            user.setId(optionalUser.get().getId());
            user.setUsername(optionalUser.get().getUsername());
            userService.save(user);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not exist");
        }
    }


}
