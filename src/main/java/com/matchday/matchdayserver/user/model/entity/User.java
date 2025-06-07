    package com.matchday.matchdayserver.user.model.entity;

    import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
    import com.matchday.matchdayserver.user.model.enums.Role;
    import com.matchday.matchdayserver.user.model.enums.SocialType;
    import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
    import jakarta.persistence.*;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.ColumnDefault;

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

        @Column(nullable = true)
        private String email;

        @Column(nullable = true)
        private String password;

        @Column(nullable = true)
        @Enumerated(EnumType.STRING)
        private Role role = Role.USER;

        @Column(nullable = true)
        @Enumerated(EnumType.STRING)
        private SocialType socialType;

        @Column(nullable = true)
        private String socialId;

        @Builder
        public User(String name, String email, String password, String profileImg,
            Role role, SocialType socialType, String socialId) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.profileImg = profileImg;
            this.role = role != null ? role : Role.USER;
            this.socialType = socialType;
            this.socialId = socialId;
        }

        public void updateName(String name) {
            this.name = name;
        }

        public void updateProfileImg(String key) {
            this.profileImg = key;
        }

        public void updateRole(Role newRole) {
            this.role = newRole;
        }

        @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
        private List<UserTeam> userTeams;

        public static User mock() {
            return User.builder()
                    .name("UNKNOWN")
                    .build();
        }
    }
