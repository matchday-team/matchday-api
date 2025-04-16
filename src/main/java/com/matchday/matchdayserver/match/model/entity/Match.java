package com.matchday.matchdayserver.match.model.entity;

import com.matchday.matchdayserver.team.model.entity.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "`match`")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private MatchType matchType;

    @Column(length = 50, nullable = false)
    private String stadium;

    @FutureOrPresent(message = "과거 일자는 등록할 수 없습니다.") //과거 일자 등록 못하게 제약
    @Column(nullable = false)
    private LocalDate matchDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    public enum MatchType {
        리그, 대회, 친선경기
    }

    @Builder
    public Match(String title, Team homeTeam, Team awayTeam, MatchType matchType, String stadium, LocalDate matchDate, LocalTime startTime, LocalTime endTime) {
        this.title = title;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchType = matchType;
        this.stadium = stadium;
        this.matchDate = matchDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

