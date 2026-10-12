package com.arena.tournaments;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService service;
    public TournamentController(TournamentService service){
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<TournamentModel>> getTournaments(){
        return ResponseEntity.ok(service.getTournaments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTournament(@PathVariable Long id){
        Optional<TournamentModel> tournament = service.getTournament(id);

        return ResponseEntity.of(tournament);
    }

    @PostMapping("")
    public ResponseEntity<TournamentModel> postTournament(@RequestBody TournamentModel tournament){
        TournamentModel model = service.createTournament(tournament);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(@PathVariable Long id, @RequestBody TournamentModel actualizedTournament){
        Optional<TournamentModel> tournament = service.updateTournament(id,actualizedTournament);

        return ResponseEntity.of(tournament);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTournament(@PathVariable Long id){
        Optional<TournamentModel> tournament = service.deleteTournament(id);

        if (tournament.isPresent()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}
