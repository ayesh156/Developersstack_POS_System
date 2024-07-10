package com.devstack.pos.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class AccessPoint implements SuperEntity {
    @Id
    @Column(name = "property_id")
    private Long propertyId;
    @Column(name = "point_name")
    private String pointName;

    @OneToMany(mappedBy = "accessPoint", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AccessPointCrud> cruds = new ArrayList<>();
}
