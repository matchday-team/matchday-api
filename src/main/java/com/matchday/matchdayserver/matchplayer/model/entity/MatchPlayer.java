package com.matchday.matchdayserver.matchplayer.model.entity;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MatchPlayer {
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

    @Column(name = "match_position", nullable = false)
    private String matchPosition;

    public enum Role {
        start_player, sub_player
    }

    @Builder
    public MatchPlayer(Match match, User user, Role role, String matchPosition) {
        this.match = match;
        this.user = user;
        this.role = role;
        this.matchPosition = matchPosition;
    }
}