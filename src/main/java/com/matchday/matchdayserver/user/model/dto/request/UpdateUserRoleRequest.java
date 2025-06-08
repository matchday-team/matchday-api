package com.matchday.matchdayserver.user.model.dto.request;

import com.matchday.matchdayserver.user.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleRequest {

    @Schema(description = "Role을 변경할 UserId")
    private Long userId;

    @Schema(description = "새로 부여할 Role", example = "USER", allowableValues = {"SUPER_ADMIN", "ADMIN", "USER"} )
    private Role role;
}