package com.arena.tournaments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class TournamentServiceTest {

    @Mock
    private TournamentRepository repository;

    @InjectMocks
    private TournamentService service;

    @Test
    @DisplayName("Não é possível criar um Torneio com nome vazio")
    void shouldNotCreateTournamentWithoutName(){
        // Arrange
        TournamentModel tournamentWithoutName = new TournamentModel("","Valorant","OPEN");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,() -> service.createTournament(tournamentWithoutName));
    }

    @Test
    @DisplayName("Não é possível criar um Torneio com nome nulo")
    void shouldNotCreateTournamentWithNullName(){
        // Arrange
        TournamentModel tournamentWithNameNull = new TournamentModel(null,"Counter Strike 2","OPEN");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,() -> service.createTournament(tournamentWithNameNull));
    }

    @Test
    @DisplayName("Deve retornar todos os torneios")
    void shouldReturnAllTournaments(){
        // Arrange
        List<TournamentModel> tournaments = new ArrayList<>(List.of(
                new TournamentModel("Valorant Champions","Valorant","OPEN"),
                new TournamentModel("Major","Counter Strike 2","OPEN")
        ));

        when(repository.findAll()).thenReturn(tournaments);

        // Act
        List<TournamentModel> result = service.getTournaments();

        // assert
        assertEquals(tournaments,result);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver torneio")
    void shouldReturnEmptyListWhenNoTournamentsExist(){
        // Arrange
        when(repository.findAll()).thenReturn(List.of());

        // Act
        List<TournamentModel> result = service.getTournaments();

        // Assert
        assertTrue(result.isEmpty());
    }


}