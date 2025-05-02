package com.matchday.matchdayserver.match.model.entity;

import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.team.model.entity.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @Column(name = "first_half_start_time")
    private LocalTime firstHalfStartTime; // 전반 시작 시간

    @Column(name = "second_half_start_time")
    private LocalTime secondHalfStartTime; // 후반 시작 시간

    @Column(name = "main_referee")
    private String mainRefereeName; //주심

    @Column(name = "assistant_referee_1")
    private String assistantReferee1; //부심1

    @Column(name = "assistant_referee_2")
    private String assistantReferee2; //부심2

    @Column(name = "fourth_referee")
    private String fourthReferee;  //대기심

    @Column(name = "home_team_memo", nullable = true)
    private String homeTeamMemo;  //홈팀 메모

    @Column(name = "away_team_memo", nullable = true)
    private String awayTeamMemo;  //홈팀 메모

    @Column(name = "match_status")
    private MatchStatus matchStatus;  //경기 상태 (시작 전, 진행 중, 종료)

    public enum MatchType {
        리그, 대회, 친선경기
    }

    public enum MatchStatus {
        SCHEDULED,  // 경기 전
        IN_PLAY,    // 진행 중
        FINISHED    //경기 종료
    }

    public void updateHomeTeamMemo(String memo) {
    this.homeTeamMemo = memo;
    }

    public void updateAwayTeamMemo(String memo) {
    this.awayTeamMemo = memo;
    }

    @Builder
    public Match(String title, Team homeTeam, Team awayTeam, MatchType matchType, String stadium, LocalDate matchDate,
        LocalTime startTime, LocalTime endTime, LocalTime firstHalfStartTime, LocalTime secondHalfStartTime, String mainRefereeName, String assistantReferee1, String assistantReferee2, String fourthReferee) {
      this.title = title;
      this.homeTeam = homeTeam;
      this.awayTeam = awayTeam;
      this.matchType = matchType;
      this.stadium = stadium;
      this.matchDate = matchDate;
      this.startTime = startTime;
      this.endTime = endTime;
      this.firstHalfStartTime = firstHalfStartTime;
      this.secondHalfStartTime = secondHalfStartTime;
      this.mainRefereeName = mainRefereeName;
      this.assistantReferee1 = assistantReferee1;
      this.assistantReferee2 = assistantReferee2;
      this.fourthReferee = fourthReferee;
    }

    @OneToMany(mappedBy = "match",cascade = CascadeType.REMOVE)
    private List<MatchEvent> matchEvents;
}

