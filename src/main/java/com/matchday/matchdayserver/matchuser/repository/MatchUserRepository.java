package com.matchday.matchdayserver.matchuser.repository;

import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchUserRepository extends JpaRepository<MatchUser, Long> {
  @Query("SELECT mu FROM MatchUser mu " +
      "JOIN FETCH mu.match m " +
      "JOIN FETCH mu.user u " +
      "WHERE mu.match.id = :matchId AND mu.user.id = :userId")
  Optional<MatchUser> findByMatchIdAndUserIdWithFetch(@Param("matchId") Long matchId, @Param("userId") Long userId);

  // 특정 유저가 참여한 매치 리스트 조회
  @Query("SELECT mu.match.id FROM MatchUser mu WHERE mu.user.id = :userId")
  List<Long> findMatchIdsByUserId(@Param("userId") Long userId);


}
