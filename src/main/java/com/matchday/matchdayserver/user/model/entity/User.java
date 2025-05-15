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

        @Column(name = "profile_img", length = 512, nullable = true)
        private String profileImg;

        @Builder
        public User (String name, String profileImg) {
            this.name = name;
            this.profileImg = profileImg;
        }

        public void updateName(String name) {
            this.name = name;
        }

        public void updateProfileImg(String key) {
            this.profileImg = key;
        }

        @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
        private List<UserTeam> userTeams;

        public static User mock() {
            return User.builder()
                    .name(null)
                    .build();
        }
    }
