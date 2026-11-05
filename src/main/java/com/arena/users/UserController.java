package com.arena.users;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> postUser(@RequestBody @Valid UserRequestDTO userRequestDTO){
        UserModel userModel = mapper.map(userRequestDTO);

        UserResponseDTO userResponseDTO = mapper.map(service.createUser(userModel));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        return ResponseEntity.ok(service.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id){
        UserResponseDTO responseDTO = service.findUserById(id);

        return ResponseEntity.ok(responseDTO);
    }

}
