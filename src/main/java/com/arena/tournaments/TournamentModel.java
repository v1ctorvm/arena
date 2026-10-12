package com.arena.tournaments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentModel {
    private Long id;
    private String name;
    private String game;
    private String status;
}
