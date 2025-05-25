package com.matchday.matchdayserver.matchevent.model.entity;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MatchEvent parent;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime eventTime;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) // 20글자 넘어가는 ENUM 생기면 수정해줘야함
    private MatchEventType eventType;

    @Column(length = 400)
    private String description; // 메모

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // EAGER 로딩이 필요하다면 변경하시오
    @JoinColumn(name = "match_id", nullable = false) // 명시적으로 외래키 명 지정
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_user_id", nullable = false)
    private MatchUser matchUser;

    public void setParent(MatchEvent parent) {
        this.parent = parent;
    }

    public MatchEvent copyWith(MatchEventType eventType) {
        return MatchEvent.builder()
                .eventType(eventType)
                .description(this.description)
                .match(this.match)
                .matchUser(this.matchUser)
                .build();
    }
}
