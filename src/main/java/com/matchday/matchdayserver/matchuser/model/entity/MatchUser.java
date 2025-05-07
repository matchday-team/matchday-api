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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchUserRole role;

    @Column(name = "match_position") // 감독 고려하여 null 허용
    private String matchPosition;

    @Min(0)
    @Max(29)
    @Column(name = "match_grid_x") // 감독 고려하여 null 허용
    private int matchGridX;

    @Min(0)
    @Max(29)
    @Column(name = "match_grid_y") // 감독 고려하여 null 허용
    private int matchGridY;

    @OneToMany(mappedBy = "matchUser", cascade = CascadeType.REMOVE)
    private List<MatchEvent> matchEvents;

    public void updateMatchPosition(String matchPosition) {
        this.matchPosition = matchPosition;
    }

    public void updateMatchGrid(int matchGridX, int matchGridY) {
        this.matchGridX = matchGridX;
        this.matchGridY = matchGridY;
    }

    public void exchange(MatchUser toMatchUser) {
        if (!toMatchUser.getTeam().getId().equals(this.team.getId())) {
            throw new ApiException(MatchStatus.DIFFERENT_TEAM_EXCHANGE);
        }
        String originalPosition = toMatchUser.getMatchPosition();
        int originalGridX = toMatchUser.getMatchGridX();
        int originalGridY = toMatchUser.getMatchGridY();
        toMatchUser.updateMatchPosition(this.matchPosition);
        toMatchUser.updateMatchGrid(this.matchGridX, this.matchGridY);

        this.matchPosition = originalPosition;
        this.matchGridX = originalGridX;
        this.matchGridY = originalGridY;
    }
}