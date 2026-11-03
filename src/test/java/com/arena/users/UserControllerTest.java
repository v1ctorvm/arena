package com.arena.users;

import com.arena.exceptions.EmailAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


}
