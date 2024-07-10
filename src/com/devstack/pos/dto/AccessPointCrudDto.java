package com.devstack.pos.dto;

import com.devstack.pos.entity.enums.Crud;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessPointCrudDto {
    private long propertyId;
    private Crud crud;
}
