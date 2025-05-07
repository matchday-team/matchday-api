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

    @Column(nullable = false)
    private String teamColor;  //팀 컬러(상의 컬러)

    @Column(nullable = false)
    private String bottomColor; //팀 하의 컬러

    @Column(nullable = false)
    private String stockingColor; //팀 스타킹 컬러

    @Column(name = "team_img", length = 512, nullable = true)
    private String teamImg;


  @Builder
    public Team (String name, String teamColor, String bottomColor, String stockingColor, String teamImg) {
        this.name = name;
        this.teamColor = teamColor;
        this.bottomColor = bottomColor;
        this.stockingColor = stockingColor;
        this.teamImg = teamImg;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateTeamImg(String key) {
        this.teamImg = key;
    }

    @OneToMany(mappedBy = "team" , cascade = CascadeType.REMOVE)
    private List<UserTeam> userTeams;
}

