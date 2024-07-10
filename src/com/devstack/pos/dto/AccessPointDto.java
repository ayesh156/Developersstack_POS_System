package com.devstack.pos.dto;

import com.devstack.pos.entity.AccessPointCrud;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccessPointDto {
    private Long propertyId;
    private String pointName;
    private List<AccessPointCrudDto> cruds = new ArrayList();
}
