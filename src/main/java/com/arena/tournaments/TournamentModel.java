package com.arena.tournaments;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_tournaments")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome não pode ser vazio")
    private String name;

    @NotBlank
    private String game;

    private String status;

    public TournamentModel(String name, String game, String status) {
        this.name = name;
        this.game = game;
        this.status = status;
    }
}
