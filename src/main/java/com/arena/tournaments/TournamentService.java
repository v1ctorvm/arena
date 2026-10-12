package com.arena.tournaments;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {

    private long nextId = 1L;

    private final List<TournamentModel> tournaments = new ArrayList<TournamentModel>(List.of(
            new TournamentModel(nextId++,"ARENA Masters","Counter-Strike 2","OPEN"),
            new TournamentModel(nextId++,"Valorant Champions","Valorant","FINISHED")
    ));

    public List<TournamentModel> getTournaments() {
        return List.copyOf(tournaments);
    }

    public Optional<TournamentModel> getTournament(Long id){
        return tournaments.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public TournamentModel createTournament(TournamentModel tournament){
        Long id = nextId++;
        tournament.setId(id);
        tournaments.add(tournament);

        return tournament;
    }

    public Optional<TournamentModel> updateTournament(Long id, TournamentModel tournamentModel){
        Optional<TournamentModel> tournament = getTournament(id);

        tournament.ifPresent(existing -> {
            existing.setName(tournamentModel.getName());
            existing.setGame(tournamentModel.getGame());
            existing.setStatus(tournamentModel.getStatus());
        });

        return tournament;
    }

    public Optional<TournamentModel> deleteTournament(Long id){
        Optional<TournamentModel> tournament = getTournament(id);

        tournament.ifPresent(tournaments::remove);

        return tournament;
    }
}
