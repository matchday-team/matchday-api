package com.matchday.matchdayserver.user.model.entity;

import com.matchday.matchdayserver.user.model.enums.Role;
import com.matchday.matchdayserver.user.model.enums.SocialType;

public final class MockUser {

    public static User createAdmin(String name) {
        return User.builder()
            .name(name)
            .email(name + "@example.com")
            .role(Role.ADMIN)
            .socialType(SocialType.GOOGLE)
            .socialId("google123")
            .build();
    }

    public static User create(String name) {
        return User.builder()
            .name(name)
            .email(name + "@example.com")
            .role(Role.USER)
            .socialType(SocialType.GOOGLE)
            .socialId("google123")
            .build();
    }

    public static User createSuperAdmin(String name) {
        return User.builder()
            .name(name)
            .email(name + "@example.com")
            .role(Role.SUPER_ADMIN)
            .socialType(SocialType.GOOGLE)
            .socialId("google123")
            .build();
    }
}