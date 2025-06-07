package com.matchday.matchdayserver.user.model.dto.response;

import com.matchday.matchdayserver.user.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRoleResponse {
    private Long userId;
    private Role updatedRole;
}
