package com.devstack.pos.entity;

import com.devstack.pos.entity.enums.Crud;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccessPointCrud {
    @Id
    private long propertyId;

    @Enumerated(EnumType.STRING)
    private Crud crud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_point")
    private AccessPoint accessPoint;
}
