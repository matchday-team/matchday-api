package com.matchday.matchdayserver.auth.mapper;

import com.matchday.matchdayserver.user.model.dto.LoginUserDto;
import com.matchday.matchdayserver.user.model.entity.User;

public class UserMapper {
    public static LoginUserDto toLoginUserDto(User user) {
        return LoginUserDto.builder().
            id(user.getId()).
            email(user.getEmail()).
            role(user.getRole()).
            socialType(user.getSocialType()).
            build();
    }
}
