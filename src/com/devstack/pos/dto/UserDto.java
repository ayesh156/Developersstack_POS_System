package com.devstack.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UserDto {

    private Long propertyId;

    private String username;

    private String password;

    private String displayName;

    private boolean activeState;
    private UserRoleDto userRoleDto;
}
