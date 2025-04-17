package com.matchday.matchdayserver.matchplayer.repository;

import com.matchday.matchdayserver.matchplayer.model.entity.MatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {
}
