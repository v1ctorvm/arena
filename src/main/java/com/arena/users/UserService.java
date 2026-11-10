package com.arena.users;

import com.arena.exceptions.EmailAlreadyExistsException;
import com.arena.exceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;


    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       UserMapper mapper) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }


    public UserModel createUser(UserModel user){

        Optional<UserModel> userModelOptional = repository.findByEmail(user.getEmail());

        if (userModelOptional.isPresent()){
            throw new EmailAlreadyExistsException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public List<UserResponseDTO> findAllUsers(){

        List<UserModel> users = repository.findAll();

        return users.stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findUserById(Long id){
        Optional<UserModel> model = repository.findById(id);

        return model.map(mapper::map).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO updateRequestDTO){
        UserModel model = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        model.setEmail(updateRequestDTO.email());
        model.setName(updateRequestDTO.name());

        repository.save(model);

        return mapper.map(model);
    }



}
