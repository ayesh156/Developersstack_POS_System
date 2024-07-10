package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.custom.AccessPointDao;
import com.devstack.pos.db.HibernateUtil;
import com.devstack.pos.entity.AccessPoint;
import com.devstack.pos.entity.AccessPointCrud;
import com.devstack.pos.entity.User;
import com.devstack.pos.entity.enums.Crud;
import com.devstack.pos.util.KeyGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AccessPointDaoImpl implements AccessPointDao {
    @Override
    public boolean create(AccessPoint accessPoint) {
        try(Session session = HibernateUtil.getSession()){
            Transaction transaction = session.beginTransaction();
            session.save(accessPoint);
            transaction.commit();
            return true;
        }
    }

    @Override
    public AccessPoint find(Long aLong) {
        return null;
    }

    @Override
    public boolean remove(Long id) {
        try(Session session = HibernateUtil.getSession()){
            Transaction transaction = session.beginTransaction();
            Query<AccessPointCrud> query = session.createQuery("FROM AccessPointCrud u WHERE u.propertyId =: id", AccessPointCrud.class);
            query.setParameter("id",id);
            AccessPointCrud accessPointCrud = query.uniqueResult();
            if(accessPointCrud!=null){
                session.remove(accessPointCrud);
                transaction.commit();
                return true;
            }else {
                throw new RuntimeException("Access Point Crud not found!");
            }

        }
    }

    @Override
    public boolean modify(AccessPoint accessPoint) {
        return false;
    }

    @Override
    public List<AccessPoint> loadAll() {
        try(Session session = HibernateUtil.getSession()) {
            Query<AccessPoint> fromAccesspoint = session.createQuery("FROM AccessPoint", AccessPoint.class);
            return fromAccesspoint.list();
        }
    }

    @Override
    public List<AccessPoint> loadAllAccessPoint(String searchText) {
        try(Session session = HibernateUtil.getSession()) {
            Query<AccessPoint> accessPointQuery = session.createQuery("SELECT a FROM AccessPoint a WHERE a.pointName LIKE :name", AccessPoint.class);
            accessPointQuery.setParameter("name","%"+searchText+"%");
            return accessPointQuery.getResultList();
        }
    }

    @Override
    public boolean setPrivileges(long privilegeId, boolean create, boolean read, boolean update, boolean delete) {
        try(Session session = HibernateUtil.getSession()){
            Transaction transaction = session.beginTransaction();
            Query<AccessPoint> query = session.createQuery("FROM AccessPoint a WHERE a.propertyId =: id", AccessPoint.class);
            query.setParameter("id", privilegeId);
            AccessPoint accessPoint = query.uniqueResult();
            if(accessPoint!=null){

                if (create){
                    AccessPointCrud c = new AccessPointCrud(
                            KeyGenerator.generateId(), Crud.CREATE,accessPoint
                    );
                    session.save(c);
                }

                if (update){
                    AccessPointCrud c = new AccessPointCrud(
                            KeyGenerator.generateId(), Crud.UPDATE,accessPoint
                    );
                    session.save(c);
                }

                if (delete){
                    AccessPointCrud c = new AccessPointCrud(
                            KeyGenerator.generateId(), Crud.DELETE,accessPoint
                    );
                    session.save(c);
                }

                if (read){
                    AccessPointCrud c = new AccessPointCrud(
                            KeyGenerator.generateId(), Crud.READ,accessPoint
                    );
                    session.save(c);
                }

                transaction.commit();
                return true;

            }else {
                throw new RuntimeException("Access Point not found!");
            }

        }
    }
}
