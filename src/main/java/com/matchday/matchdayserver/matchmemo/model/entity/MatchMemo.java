package com.matchday.matchdayserver.matchmemo.model.entity;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.team.model.entity.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchMemo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Match match;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Team team;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String memo;

  public void updateMemo(String newMemo) {
    this.memo = newMemo;
  }
}