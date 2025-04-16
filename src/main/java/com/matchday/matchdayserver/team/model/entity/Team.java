package com.matchday.matchdayserver.team.model.entity;

import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(nullable = false)
    @CreationTimestamp //추후 필요하다면 EntityListener 추가
    private LocalDateTime createdAt;

    @Builder
    public Team (String name) {
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "team" , cascade = CascadeType.REMOVE)
    private List<UserTeam> userTeams;
}

