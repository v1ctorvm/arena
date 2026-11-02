package com.arena.tournaments;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {

    private final TournamentRepository repository;

    public TournamentService(TournamentRepository repository) {
        this.repository = repository;
    }

    public List<TournamentModel> getTournaments() {
        return List.copyOf(repository.findAll());
    }

    public Optional<TournamentModel> getTournament(Long id){
        return repository.findById(id);
    }

    public TournamentModel createTournament(TournamentModel tournament) {

        validateTournamentName(tournament.getName());

        return repository.save(tournament);
    }

    @Transactional
    public Optional<TournamentModel> updateTournament(Long id, TournamentModel tournamentModel){

        validateTournamentName(tournamentModel.getName());

        Optional<TournamentModel> tournament = repository.findById(id);

        tournament.ifPresent(existing -> {
            existing.setName(tournamentModel.getName());
            existing.setGame(tournamentModel.getGame());
            existing.setStatus(tournamentModel.getStatus());

        });
        return tournament;
    }

    public Optional<TournamentModel> deleteTournament(Long id){
        Optional<TournamentModel> tournament = getTournament(id);

        tournament.ifPresent(t ->
                repository.deleteById(id));

        return tournament;
    }

    private void validateTournamentName(String name){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Empty name");
        }
    }
}
