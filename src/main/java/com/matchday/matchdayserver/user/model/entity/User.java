    package com.matchday.matchdayserver.user.model.entity;

    import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
    import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
    import jakarta.persistence.*;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    import java.util.List;

    @Entity
    @Getter
    @NoArgsConstructor
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 30, unique = true)
        private String name;

        @Builder
        public User (String name) {
            this.name = name;
        }

        public void updateName(String name) {
            this.name = name;
        }

        @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
        private List<UserTeam> userTeams;

        @OneToMany(mappedBy = "user") // cascade = CascadeType.REMOVE 안한 이유 : 유저의 과거 기록은 남겨두는게 좋을듯?
        private List<MatchEvent> matchEvents;
    }
