package org.ac.cst8277.kirk.patrick.usermanagementservice.dao;

import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Role;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.User;

import java.util.List;
import java.util.UUID;

public interface UserDatabase {
    void open();
    void close();

    void insertUser(User user);
    List<User> getAllUsers();
    List<User> getPublishers();
    List<User> getSubscribers();
    User getUserById(UUID id);
    List<User> getManyById(List<UUID> ids);
    User getUserByUsername(String name);
    User getUserByToken(UUID token);
    void updateUser(User user);
    void deleteUser(UUID id);

    void createRole(Role role);
    void updateRole(Role role);
    void deleteRole(String roleName);
    void addRole(UUID userId, String role);
    void removeRole(UUID userId, String role);
}
