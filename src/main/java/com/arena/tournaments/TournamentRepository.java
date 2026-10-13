package com.arena.tournaments;


import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<TournamentModel, Long> {
}
