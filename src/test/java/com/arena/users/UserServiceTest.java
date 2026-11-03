package com.arena.users;

import com.arena.exceptions.EmailAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService service;



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
        UserModel user = new UserModel("Jhon Doe", "jdoe@mail.com","strongpass");

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

        UserModel user  = new UserModel("Jhon Doe", "Jhon@gmail.com","password");

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
        service = new UserService(repository,realEncoder);

        UserModel user = new UserModel("Jhon","Jhon@mail.com","Arena123");

        when(repository.save(user)).thenReturn(user);

        // Act
        service.createUser(user);

        // Assert
        assertTrue(realEncoder.matches("Arena123", user.getPassword()));


    }

}
