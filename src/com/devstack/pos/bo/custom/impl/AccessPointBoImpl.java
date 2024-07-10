package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.bo.custom.AccessPointBo;
import com.devstack.pos.dao.custom.AccessPointDao;
import com.devstack.pos.dto.AccessPointCrudDto;
import com.devstack.pos.dto.AccessPointDto;
import com.devstack.pos.entity.AccessPoint;
import com.devstack.pos.entity.AccessPointCrud;

import java.util.ArrayList;
import java.util.List;

public class AccessPointBoImpl implements AccessPointBo {
    private AccessPointDao accessPointDao = DaoFactory.getDao(DaoFactory.DaoType.ACCESS_POINT);
    @Override
    public boolean createAccessPoint(AccessPointDto dto) {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setPropertyId(dto.getPropertyId());
        accessPoint.setPointName(dto.getPointName());
        return accessPointDao.create(accessPoint);
    }

    @Override
    public boolean setPrivileges(long privilegeId, boolean create, boolean read, boolean update, boolean delete) {
        return accessPointDao.setPrivileges(privilegeId, create, read, update, delete);
    }

    @Override
    public List<AccessPointDto> loadAll() {
        List<AccessPoint> accessPoints = accessPointDao.loadAll();
        List<AccessPointDto> dtos = new ArrayList<>();

        for (AccessPoint  p:accessPoints
             ) {
            AccessPointDto accessPointDto = new AccessPointDto();
            accessPointDto.setPointName(p.getPointName());
            accessPointDto.setPropertyId(p.getPropertyId());
            List<AccessPointCrudDto> cruds = new ArrayList<>();
            for (AccessPointCrud c: p.getCruds()
                 ) {
                cruds.add(new AccessPointCrudDto(c.getPropertyId(),c.getCrud()));
            }

            accessPointDto.setCruds(cruds);
            dtos.add(accessPointDto);
        }
        return dtos;
    }

    @Override
    public List<AccessPointDto> loadAllAccessPoint(String searchText) {
        List<AccessPoint> accessPoints = accessPointDao.loadAllAccessPoint(searchText);
        List<AccessPointDto> dtos = new ArrayList<>();

        for (AccessPoint  p:accessPoints
        ) {
            AccessPointDto accessPointDto = new AccessPointDto();
            accessPointDto.setPointName(p.getPointName());
            accessPointDto.setPropertyId(p.getPropertyId());
            List<AccessPointCrudDto> cruds = new ArrayList<>();
            for (AccessPointCrud c: p.getCruds()
            ) {
                cruds.add(new AccessPointCrudDto(c.getPropertyId(),c.getCrud()));
            }

            accessPointDto.setCruds(cruds);
            dtos.add(accessPointDto);
        }
        return dtos;
    }

    @Override
    public boolean dropPrivilage(Long privilegeId) {
        return accessPointDao.remove(privilegeId);
    }
}
