package com.matchday.matchdayserver.matchuser.model.entity;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
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

    @Column(name = "match_position") //감독 고려하여 null 허용
    private String matchPosition;

    @Column(name = "match_grid") //감독 고려하여 null 허용
    private String matchGrid;

    public enum Role {
        admin, start_player, sub_player //감독, 선발선수, 후발선수
    }

}