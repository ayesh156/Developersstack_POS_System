package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.custom.UserDao;
import com.devstack.pos.db.HibernateUtil;
import com.devstack.pos.entity.User;
import com.devstack.pos.entity.UserRole;
import com.devstack.pos.util.KeyGenerator;
import com.devstack.pos.util.PasswordGenerator;
import com.devstack.pos.util.ResponseData;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean create(User user) {
        try(Session session = HibernateUtil.getSession()){
            session.save(user);
            session.close();
        }
        return true;
    }

    @Override
    public User find(Long id) {
        return null;
    }

    @Override
    public boolean remove(Long id) {

        try(Session session = HibernateUtil.getSession()){
            Transaction transaction = session.beginTransaction();
            Query<User> query = session.createQuery("FROM User u WHERE u.propertyId =: id", User.class);
            query.setParameter("id",id);
            User user = query.uniqueResult();
            if(user!=null){
                session.remove(user);
                transaction.commit();
                return true;
            }else {
                throw new RuntimeException("User not found!");
            }

        }

    }

    @Override
    public boolean modify(User user) {
        return false;
    }

    @Override
    public List<User> loadAll() {
        return null;
    }

    @Override
    public ResponseData login(String username, String password) {
        try(Session session = HibernateUtil.getSession()){
            Query<User> query = session.createQuery("FROM User u WHERE u.username =: username", User.class);
            query.setParameter("username",username);
            User user = query.uniqueResult();
            if(user!=null && user.isActiveState()){
                if(PasswordGenerator.checkPassword(password,user.getPassword())){
                    return new ResponseData(true,"login success!");
                }else {
                    return new ResponseData(false,"password is wrong!");
                }
            }else {
                return new ResponseData(false,"something went wrong with the Username or the active state, please contact the admin");
            }

        }
    }

    @Override
    public List<User> loadAllUsers(String searchText) {
        try(Session session = HibernateUtil.getSession()){
            Query<User> userQuery = session.createQuery("SELECT u FROM User u WHERE u.displayName LIKE :name", User.class);
            userQuery.setParameter("name","%"+searchText+"%");
            return userQuery.getResultList();
        }
    }

    @Override
    public void createNewSystemUser(Long userRoleId, String displayName, String email) {

        try(Session session = HibernateUtil.getSession()) {
            // user email
            Query<User> query = session.createQuery("FROM User u WHERE u.username =: username",User.class);
            query.setParameter("username",email);

            User user = query.uniqueResult();
            if(user!=null){
                throw new RuntimeException("User Already Exists!");
            }

            // check userRole
            Query<UserRole> roleQuery = session.createQuery("FROM UserRole u WHERE u.propertyId =: id",UserRole.class);
            roleQuery.setParameter("id",userRoleId);
            UserRole userRole = roleQuery.uniqueResult();

            if(userRole==null){
                throw new RuntimeException("User Role not found!");
            }

            // save
            User createUser = new User(
                    KeyGenerator.generateId(),
                    email,
                    PasswordGenerator.passwordGen(6),
                    displayName,
                    true,
                    userRole
            );
            Transaction transaction = session.beginTransaction();
            session.save(createUser);
            transaction.commit();
            System.out.println(createUser.getPassword());
        }

    }

    @Override
    public void updateSystemUser(Long userRoleId, String displayName, String email, Long userId) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            // Fetch the user
            Query<User> query = session.createQuery("FROM User u WHERE u.propertyId =: id", User.class);
            query.setParameter("id", userId);
            User user = query.uniqueResult();

            // Check if user is not found
            if (user == null) {
                throw new RuntimeException("User not found!");
            }

            // Fetch the user role
            Query<UserRole> roleQuery = session.createQuery("FROM UserRole u WHERE u.propertyId =: roleId", UserRole.class);
            roleQuery.setParameter("roleId", userRoleId);
            UserRole userRole = roleQuery.uniqueResult();

            // Check if user role is not found
            if (userRole == null) {
                throw new RuntimeException("User Role not found!");
            }

            // Update user properties
            user.setUserRole(userRole);
            user.setDisplayName(displayName);
            user.setUsername(email);

            // Now check if user is not null before updating
            if (user != null) {
                session.update(user);
                transaction.commit();
            } else {
                // This block is not reachable because if user is null, an exception has been thrown earlier.
                throw new RuntimeException("Unexpected error: User is null!");
            }
        }
    }
}
