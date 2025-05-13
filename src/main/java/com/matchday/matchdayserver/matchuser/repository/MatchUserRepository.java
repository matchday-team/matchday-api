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
      "JOIN FETCH mu.team t " +
      "WHERE mu.match.id = :matchId AND mu.user.id = :userId")
  Optional<MatchUser> findByMatchIdAndUserIdWithFetch(@Param("matchId") Long matchId, @Param("userId") Long userId);

  @Query("SELECT mu FROM MatchUser mu " +
      "JOIN FETCH mu.match m " +
      "JOIN FETCH mu.team t " +
      "WHERE mu.match.id = :matchId AND mu.team.id = :teamId AND mu.user.id IS NULL")
  Optional<MatchUser> findTempUser(@Param("matchId") Long matchId, @Param("teamId") Long teamId);

  //특정 매치에 특정 유저가 존재하는지 여부
  boolean existsByMatchIdAndUserId(Long matchId, Long userId);
  
  // 특정 유저가 참여한 매치 리스트 조회
  @Query("SELECT mu.match.id FROM MatchUser mu WHERE mu.user.id = :userId")
  List<Long> findMatchIdsByUserId(@Param("userId") Long userId);

  /**
   * 감독/기록관 제외, 임시 유저 제외
   * @param matchId
   * @return
  */
  List<MatchUser> findByMatchIdAndTeamIdIsNotNullAndUserIdIsNotNull(Long matchId);
}
