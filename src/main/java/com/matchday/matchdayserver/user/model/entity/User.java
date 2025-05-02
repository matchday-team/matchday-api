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

        @Column(nullable = false, length = 30)
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
    }
