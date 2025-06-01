package com.matchday.matchdayserver.user.model.dto;

import com.matchday.matchdayserver.user.model.enums.Role;
import com.matchday.matchdayserver.user.model.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserDto {
    private Long id;
    private String email;
    private Role role;
    private SocialType socialType;
}
