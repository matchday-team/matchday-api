package com.matchday.matchdayserver.userteam.model.entity;

import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.userteam.model.dto.JoinUserTeamResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer number;

    @Column
    private String defaultPosition; //기획이 명확하지 않아 추후 자료형 변경 가능성 있음

    @Column
    @CreationTimestamp
    private LocalDateTime joinDate; //팀 입단 날짜

    @Column
    private LocalDateTime leaveDate; //팀 떠난 날짜

    @Column
    private Boolean isActive; //현재 팀에서 활동 여부

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //EAGER 로딩이 필요하다면 변경하시오
    @JoinColumn(name = "user_id", nullable = false) //명시적으로 외래키 명 지정
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private User user;

    @Builder
    public UserTeam (Team team,User user,Integer number, String defaultPosition,Boolean isActive) {
        this.team = team;
        this.user = user;
        this.number = number;
        this.defaultPosition = defaultPosition;
        this.isActive = isActive;
    }

    public void updateNumber(Integer number) {
        this.number = number;
    }
    public void updateDefaultPosition(String defaultPosition) { this.defaultPosition = defaultPosition; }
    public void setLeaveDate(LocalDateTime leaveDate) { this.leaveDate = leaveDate; }
    public void updateIsActive(Boolean isActive) { this.isActive = isActive; }
}
