package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            log.info("requestType: CreateUserRequest, message: Triggering user creation request for username: " + createUserRequest.getUsername());
            User user = new User();
            user.setUsername(createUserRequest.getUsername());
            Cart cart = new Cart();
            cartRepository.save(cart);
            user.setCart(cart);

            if (createUserRequest.getPassword().length() < 7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
                log.error("requestType: CreateUserRequest, message: Password does not satisfy the minimum requirements");
                return new ResponseEntity("Password does not fulfill the minimum requirements", HttpStatus.BAD_REQUEST);
            }

            user.setPassword((bCryptPasswordEncoder.encode(createUserRequest.getPassword())));
            if (userRepository.findByUsername(createUserRequest.getUsername()) != null) {
                log.error("requestType: CreateUserRequest, message: Username already exists");
                return new ResponseEntity("Username already exists", HttpStatus.BAD_REQUEST);

            } else {
                userRepository.save(user);
                log.info("requestType: CreateUserRequest, message: User " + createUserRequest.getUsername() + " created successfully");
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            log.error("requestType: CreateUserRequest, message: Processing failure");
            return new ResponseEntity("Server failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
