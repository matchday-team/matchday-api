package com.matchday.matchdayserver.matchevent.model.entity;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime eventTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20) //20글자 넘어가는 ENUM 생기면 수정해줘야함
    private EventType eventType;

    @Column(length = 400)
    private String description; // 메모

    public enum EventType {
        GOAL, ASSIST, SHOT, VALID_SHOT, FOUL, OFFSIDE,
        SUB_IN, SUB_OUT, YELLOW_CARD, RED_CARD, OWN_GOAL
        //골,어시스트,슛,유효슛,파울,오프사이드
        //교체입장,교체퇴장,옐로카드,레드카드(퇴장),자책골
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //EAGER 로딩이 필요하다면 변경하시오
    @JoinColumn(name = "match_id", nullable = false) //명시적으로 외래키 명 지정
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
