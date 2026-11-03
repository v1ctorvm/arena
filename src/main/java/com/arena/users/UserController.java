package com.arena.users;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("")
    public ResponseEntity<UserResponseDTO> postUser(@RequestBody @Valid UserRequestDTO userRequestDTO){
        UserModel userModel = mapper.map(userRequestDTO);

        UserResponseDTO userResponseDTO = mapper.map(service.createUser(userModel));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponseDTO);

    }

}
