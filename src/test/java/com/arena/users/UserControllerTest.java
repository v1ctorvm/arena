package com.arena.users;

import com.arena.exceptions.EmailAlreadyExistsException;
import com.arena.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest // carrega o contexto da aplicação Spring para o teste.
//@AutoConfigureMockMvc // configura o MockMvc para simular requisições HTTP.

// Usamos @WebMvcTest em vez de @SpringBootTest porque queremos testar
// apenas a camada web (Controller), sem carregar toda a aplicação
// nem depender de uma conexão real com o banco de dados.
@WebMvcTest(UserController.class)
@Import(UserMapper.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // cria um mock e o registra no contexto de teste do Spring.
    private UserService service;

    @Test
    @DisplayName("Deve retornar 409 quando o email já estiver cadastrado")
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        // Arrange
        when(service.createUser(any(UserModel.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already in use"));

        // Act
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
        {
            "name": "Jhon Doe",
            "email": "jhon@mail.com",
            "password": "Arena123"
        }
        """)
        ).andExpect(status().isConflict());

    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void shouldReturnAllUsers() throws Exception {
        // Arrange
        List<UserResponseDTO> users = new ArrayList<>(List.of(
                new UserResponseDTO(1L,"John","Jhon@mail.com"),
                new UserResponseDTO(2L,"Jane","Jane@mail.com")
        ));

        when(service.findAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$[0].password").doesNotExist());

    }

    @Test
    @DisplayName("Deve listar apenas o usuario com o ID buscado")
    void shouldReturnUserById() throws Exception {
        // Arrange
        UserResponseDTO userResponse = new UserResponseDTO(1L,"John","jhon@mail.com");

        when(service.findUserById(1L)).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.password").doesNotExist());

    }

    @Test
    @DisplayName("Deve retornar 404 quando o usuário não existir")
    void shouldReturnNotFoundWhenUserNotFound() throws Exception {
        // Arrange
        when(service.findUserById(99L))
                .thenThrow(new UserNotFoundException("User with id 99 not found"));

        // Act & Assert
        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }


}
