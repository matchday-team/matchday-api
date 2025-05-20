package com.matchday.matchdayserver.match.model.entity;

import com.matchday.matchdayserver.match.model.enums.MatchState;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.team.model.entity.Team;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private LocalDate matchDate;

    @Column(name = "planned_start_time", nullable = false)
    private LocalTime plannedStartTime;

    @Column(name = "planned_end_time", nullable = false)
    private LocalTime plannedEndTime;

    @Column(name = "first_half_start_time")
    private LocalTime firstHalfStartTime; // 전반 시작 시간

    @Column(name = "first_half_end_time")
    private LocalTime firstHalfEndTime; // 전반 종료 시간

    @Column(name = "first_half_period", nullable = false)
    private Integer firstHalfPeriod; // 전반 진행 시간 (분)

    @Column(name = "second_half_start_time")
    private LocalTime secondHalfStartTime; // 후반 시작 시간

    @Column(name = "second_half_end_time")
    private LocalTime secondHalfEndTime; // 후반 종료 시간

    @Column(name = "second_half_period", nullable = false)
    private Integer secondHalfPeriod; // 후반 진행 시간 (분)

    @Column(name = "main_referee")
    private String mainRefereeName; //주심

    @Column(name = "assistant_referee_1")
    private String assistantReferee1; //부심1

    @Column(name = "assistant_referee_2")
    private String assistantReferee2; //부심2

    @Column(name = "fourth_referee")
    private String fourthReferee;  //대기심

    @Column(name = "memo", nullable = true)
    private String memo;  //경기 메모

    @Enumerated(EnumType.STRING)
    @Column(name = "match_state", nullable = false, length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'SCHEDULED'")
    private MatchState matchState;  //경기 상태 (시작 전, 진행 중, 종료)

    public enum MatchType {
        리그, 대회, 친선경기
    }

    public void updateMemo(String memo) {
    this.memo = memo;
    }

    public void setFirstHalfStartTime(LocalTime time) {
        this.firstHalfStartTime = time;
    }

    public void setFirstHalfEndTime(LocalTime time) {
        this.firstHalfEndTime = time;
    }

    public void setSecondHalfStartTime(LocalTime time) {
        this.secondHalfStartTime = time;
    }

    public void setSecondHalfEndTime(LocalTime time) {
        this.secondHalfEndTime = time;
    }

    public void setMatchState(MatchState state) {
        this.matchState = state;
    }


    @Builder
    public Match(String title, Team homeTeam, Team awayTeam, MatchType matchType, String stadium, LocalDate matchDate,
        LocalTime plannedStartTime, LocalTime plannedEndTime, LocalTime firstHalfStartTime, LocalTime firstHalfEndTime,
        LocalTime secondHalfStartTime, LocalTime secondHalfEndTime, String mainRefereeName,
        String assistantReferee1, String assistantReferee2, String fourthReferee,
        Integer firstHalfPeriod, Integer secondHalfPeriod) {

        this.title = title;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchType = matchType;
        this.stadium = stadium;
        this.matchDate = matchDate;
        this.plannedStartTime = plannedStartTime;
        this.plannedEndTime = plannedEndTime;
        this.firstHalfStartTime = firstHalfStartTime;
        this.firstHalfEndTime = firstHalfEndTime;
        this.secondHalfStartTime = secondHalfStartTime;
        this.secondHalfEndTime = secondHalfEndTime;
        this.mainRefereeName = mainRefereeName;
        this.assistantReferee1 = assistantReferee1;
        this.assistantReferee2 = assistantReferee2;
        this.fourthReferee = fourthReferee;
        this.matchState = MatchState.SCHEDULED; //매치 생성시 "SCHEDULED" 기본 값으로 설정
        this.firstHalfPeriod = firstHalfPeriod;
        this.secondHalfPeriod = secondHalfPeriod;
    }

    @OneToMany(mappedBy = "match",cascade = CascadeType.REMOVE)
    private List<MatchEvent> matchEvents;
}

