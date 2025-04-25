package com.matchday.matchdayserver.matchuser.model.entity;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "match_position") // 감독 고려하여 null 허용
    private String matchPosition;

    @Column(name = "match_grid") // 감독 고려하여 null 허용
    private String matchGrid;

    public enum Role {
        ADMIN, START_PLAYER, SUB_PLAYER, ARCHIVES // 감독, 선발선수, 후발선수, 기록관
    }

    @Builder
    public MatchUser(Match match, User user, Role role, String matchPosition, String matchGrid) {
        this.match = match;
        this.user = user;
        this.role = role;
        this.matchPosition = matchPosition;
        this.matchGrid = matchGrid;
    }
}