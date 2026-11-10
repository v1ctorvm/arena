package com.arena.users;

import com.arena.exceptions.EmailAlreadyExistsException;
import com.arena.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService service;

    @Spy
    private UserMapper mapper = new UserMapper();





    @Test
    @DisplayName("Nao é possível criar um Usuário com um email que ja esta em uso")
    void shouldNotCreateUserIfEmailIsAlreadyInUse(){
        // Arrange
        UserModel user = new UserModel("v1ctorvm","v1ctor@gmail.com","strongpass");

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserModel userWithRepeatedMail = new UserModel("Lucy","v1ctor@gmail.com","123456");

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> service.createUser(userWithRepeatedMail));
    }

    @Test
    @DisplayName("Deve criar o usuário corretamente")
    void shouldCreateUser(){
        // Arrange
        UserModel user = new UserModel("John Doe", "jdoe@mail.com","strongpass");

        when(repository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        when(repository.save(user))
                .thenReturn(user);


        // Act
        UserModel result = service.createUser(user);

        // Assert
        assertEquals(user,result);
    }

    @Test
    @DisplayName("Deve codificar a senha antes de salvar o usuário")
    void shouldEncodePasswordBeforeSaving() {
        // Arrange
        UserModel user = new UserModel("Victor","victor@mail.com","123456");

        when(encoder.encode(user.getPassword())).thenReturn("xxxx");

        when(repository.save(user)).thenReturn(user);

        // Act
        service.createUser(user);

        // Assert
        assertEquals("xxxx",user.getPassword());
        verify(repository).save(user);
    }

    @Test
    @DisplayName("Deve criar um hash para a senha")
    void shouldEncodePasswordWithBCrypt(){
        // Arrange
        encoder = new BCryptPasswordEncoder();

        UserModel user  = new UserModel("John Doe", "Jhon@gmail.com","password");

        // Act
        String hashedPassword = encoder.encode(user.getPassword());

        // Assert
        assertTrue(encoder.matches(user.getPassword(), hashedPassword));
        assertFalse(encoder.matches("senhaErrada", hashedPassword));
    }

    @Test
    @DisplayName("Deve cadastrar usuário com a senha armazenada como hash")
    void shouldCreateUserWithHashedPassword() {
        // Arrange
        PasswordEncoder realEncoder = new BCryptPasswordEncoder();
        service = new UserService(repository, realEncoder, mapper);

        UserModel user = new UserModel("John","Jhon@mail.com","Arena123");

        when(repository.save(user)).thenReturn(user);

        // Act
        service.createUser(user);

        // Assert
        assertTrue(realEncoder.matches("Arena123", user.getPassword()));
    }

    @Test
    @DisplayName("Deve retornar todos os usuarios cadastrados")
    void shouldReturnAllUsers(){
        // Arrange
        List<UserModel> users = new ArrayList<>(List.of(
                new UserModel("John","Jhon@mail.com","Arena123"),
                new UserModel("Jane","Jane@mail.com","Arena123")
        ));


        when(repository.findAll()).thenReturn(users);

        // Act
        List<UserResponseDTO> response = service.findAllUsers();

        // Assert
        assertEquals(2, response.size());

        assertEquals("John", response.get(0).name());
        assertEquals("Jane", response.get(1).name());

    }

    @Test
    @DisplayName("Deve retornar apenas o usuário com o ID buscado")
    void shouldReturnUserById(){
        // Arrange
        UserModel user = new UserModel(1L,"John","Jhon@mail.com","Arena123");

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserResponseDTO response = service.findUserById(1L);

        // Assert
        assertEquals(user.getId(),response.id());
        assertEquals(user.getName(),response.name());
        assertEquals(user.getEmail(),response.email());

    }

    @Test
    @DisplayName("Deve retornar uma exception se o usuario buscado pelo ID nao existe")
    void shouldThrowExceptionWhenUserNotFound(){
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> service.findUserById(99L));
    }

    @Test
    @DisplayName("Deve atualizar os dados de um usuario existente")
    void shouldUpdateExistingUser(){
        // Arrange
        UserModel user = new UserModel(1L,"John","jhon@mail.com","password");

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        UserUpdateRequestDTO userUpdateRequest = new UserUpdateRequestDTO("John Updated","john.updated@mail.com");

        // Act
        UserResponseDTO response = service.updateUser(1L, userUpdateRequest);

        // Assert
        verify(repository).save(user);
        assertEquals(1L,response.id());
        assertEquals("John Updated",response.name());
        assertEquals("john.updated@mail.com",response.email());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar um usuário inexistente")
    void shouldThrowExceptionWhenUpdatingNonexistentUser() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        UserUpdateRequestDTO userUpdateRequest = new UserUpdateRequestDTO("John Updated","john.updated@mail.com");

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> service.updateUser(99L,userUpdateRequest));
        verify(repository, never()).save(any(UserModel.class));
    }



}
