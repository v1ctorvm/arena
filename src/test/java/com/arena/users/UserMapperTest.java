package com.arena.users;

import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class UserMapperTest {

    @Test
    @DisplayName("Deve converter UserModel para UserResponseDTO")
    void shouldConvertUserModelToResponseDTO() {
        // Arrange
        UserMapper mapper = new UserMapper();
        UserModel model = new UserModel("Jhon","Doe@mail.com","123");
        model.setId(1L);

        // Act
        UserResponseDTO result = mapper.map(model);

        // Assert
        assertEquals(model.getId(),result.id());
        assertEquals(model.getName(),result.name());
        assertEquals(model.getEmail(),result.email());
    }

    @Test
    @DisplayName("Deve converter UserRequestDTO para UserModel")
    void shouldConvertUserRequestDTOToUserModel() {
        // Arrange
        UserMapper mapper = new UserMapper();
        UserRequestDTO requestDTO = new UserRequestDTO("Jhon Doe", "jhon@mail.com", "password");

        // Act
        UserModel result = mapper.map(requestDTO);

        // Assert
        assertEquals(requestDTO.name(), result.getName());
        assertEquals(requestDTO.email(), result.getEmail());
        assertEquals(requestDTO.password(), result.getPassword());
    }
}
