package com.suncloudstorage.rest;

import com.suncloudstorage.dto.UserDTO;
import com.suncloudstorage.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("users/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("username") String username) {
        return userRepository.findByUsername(username)
                .map(user -> ResponseEntity.ok().body(UserDTO.fromUser(user)))
                .orElseGet(() -> ResponseEntity.ok().body(new UserDTO()));
    }
}
