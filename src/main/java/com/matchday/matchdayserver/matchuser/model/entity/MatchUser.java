package com.matchday.matchdayserver.matchuser.model.entity;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchuser.model.enums.MatchUserRole;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.user.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // user id null일 경우 임시 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchUserRole role;

    @Column(name = "match_position") // 감독 고려하여 null 허용
    private String matchPosition;

    @Min(value = 0)
    @Max(value = 29)
    @Column(name = "match_grid") // 감독 고려하여 null 허용
    private Integer matchGrid;

    @OneToMany(mappedBy = "matchUser", cascade = CascadeType.REMOVE)
    private List<MatchEvent> matchEvents;

    public void updateMatchGrid(int matchGrid) {
        this.matchGrid = matchGrid;
    }

    public void substituteTo(MatchUser subInPlayer) {
        if (!subInPlayer.getTeam().getId().equals(this.team.getId())) {
            throw new ApiException(MatchStatus.DIFFERENT_TEAM_EXCHANGE);
        }

        subInPlayer.updateMatchGrid(this.matchGrid);
    }


    // 퇴장시키는 메서드
    public void sendOff() {
        this.matchPosition = null;
        this.matchGrid = null;
    }
}