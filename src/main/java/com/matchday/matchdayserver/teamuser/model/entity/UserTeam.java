package com.matchday.matchdayserver.teamuser.model.entity;

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

    @Builder
    public UserTeam (Integer number, String defaultPosition) {
        this.number = number;
        this.defaultPosition = defaultPosition;
    }

    public void updateNumber(Integer number) {
        this.number = number;
    }
    public void updateDefaultPosition(String defaultPosition) { this.defaultPosition = defaultPosition; }
    public void setLeaveDate(LocalDateTime leaveDate) { this.leaveDate = leaveDate; }
    public void updateIsActive(Boolean isActive) { this.isActive = isActive; }
}
