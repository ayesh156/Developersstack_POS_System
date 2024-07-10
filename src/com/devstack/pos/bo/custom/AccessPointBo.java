package com.devstack.pos.bo.custom;

import com.devstack.pos.dto.AccessPointDto;
import com.devstack.pos.entity.AccessPoint;

import java.util.List;

public interface AccessPointBo {
    public boolean createAccessPoint(AccessPointDto dto);

    public boolean setPrivileges(long privilegeId, boolean create, boolean read, boolean update, boolean delete);
    public List<AccessPointDto> loadAll();

    public List<AccessPointDto> loadAllAccessPoint(String searchText);

    public boolean dropPrivilage(Long privilegeId);

}
