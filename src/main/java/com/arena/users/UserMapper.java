package com.arena.users;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserModel map(UserRequestDTO userRequestDTO){
        UserModel model = new UserModel();

        model.setName(userRequestDTO.name());
        model.setEmail(userRequestDTO.email());
        model.setPassword(userRequestDTO.password());

        return model;
    }

    public UserResponseDTO map(UserModel userModel){
        return new UserResponseDTO(userModel.getId(), userModel.getName(), userModel.getEmail());
    }
}
