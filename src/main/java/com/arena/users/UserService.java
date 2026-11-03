package com.arena.users;

import com.arena.exceptions.EmailAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel createUser(UserModel user){

        Optional<UserModel> userModelOptional = repository.findByEmail(user.getEmail());

        if (userModelOptional.isPresent()){
            throw new EmailAlreadyExistsException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }




}
