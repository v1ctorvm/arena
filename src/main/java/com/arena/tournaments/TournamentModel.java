package com.arena.tournaments;

import jakarta.persistence.*;
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

    private String name;
    private String game;
    private String status;
}
