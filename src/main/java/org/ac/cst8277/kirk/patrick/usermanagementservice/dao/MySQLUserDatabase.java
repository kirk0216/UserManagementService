package org.ac.cst8277.kirk.patrick.usermanagementservice.dao;

import org.ac.cst8277.kirk.patrick.usermanagementservice.Utils;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Role;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLUserDatabase implements UserDatabase {
    private static final String CONNECTION_STRING = "jdbc:mysql://mysql:3306/ums";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "password";

    private Connection connection;

    public void open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASS);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.INSERT);
            statement.setBytes(1, Utils.toBytes(user.getId()));
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.GET_ALL);
            ResultSet resultSet = statement.executeQuery();

            User user = null;

            while (resultSet.next()) {
                if (user == null || !resultSet.getString("email").equals(user.getEmail())) {
                    user = User.getFromResultSet(resultSet);
                    users.add(user);
                }

                user.addRole(resultSet.getString("role_name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User getUserById(UUID id) {
        User user = null;

        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.GET_BY_ID);
            statement.setBytes(1, Utils.toBytes(id));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (user == null) {
                    UUID userId = Utils.toUUID(resultSet.getBytes("id"));
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");

                    user = new User(userId, username, email, password);
                }

                user.addRole(resultSet.getString("role_name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public List<User> getManyById(List<UUID> ids) {
        List<User> users = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.GET_SUBSCRIBERS_TO);

            for (UUID id : ids) {
                statement.setBytes(1, Utils.toBytes(id));
                ResultSet resultSet = statement.executeQuery();

                User user = null;

                while (resultSet.next()) {
                    if (user == null || !resultSet.getString("email").equals(user.getEmail())) {
                        user = User.getFromResultSet(resultSet);
                        users.add(user);
                    }

                    user.addRole(resultSet.getString("role_name"));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User getUserByUsername(String name) {
        User user = null;

        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.GET_BY_USERNAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (user == null) {
                    UUID userId = Utils.toUUID(resultSet.getBytes("id"));
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");

                    user = new User(userId, username, email, password);
                }

                user.addRole(resultSet.getString("role_name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Get the user by their authentication token. For testing purposes, this is just their user id.
     * @param token The user's authentication token.
     * @return A user object mapped to the authentication token.
     */
    @Override
    public User getUserByToken(UUID token) {
        return getUserById(token);
    }

    @Override
    public List<User> getPublishers() {
        List<User> users = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.GET_PUBLISHERS);
            ResultSet resultSet = statement.executeQuery();

            User user = null;

            while (resultSet.next()) {
                if (user == null || !resultSet.getString("email").equals(user.getEmail())) {
                    user = User.getFromResultSet(resultSet);
                    users.add(user);
                }

                user.addRole(resultSet.getString("role_name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public List<User> getSubscribers() {
        List<User> users = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.GET_SUBSCRIBERS);
            ResultSet resultSet = statement.executeQuery();

            User user = null;

            while (resultSet.next()) {
                if (user == null || !resultSet.getString("email").equals(user.getEmail())) {
                    user = User.getFromResultSet(resultSet);
                    users.add(user);
                }

                user.addRole(resultSet.getString("role_name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void updateUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.UPDATE);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, Utils.toBytes(user.getId()));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(UUID id) {
        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.DELETE);
            statement.setBytes(1, Utils.toBytes(id));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createRole(Role role) {
        try {
            PreparedStatement statement = connection.prepareStatement(RoleSQL.INSERT);
            statement.setBytes(1, Utils.toBytes(role.getId()));
            statement.setString(2, role.getName());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRole(Role role) {
        try {
            PreparedStatement statement = connection.prepareStatement(RoleSQL.UPDATE);
            statement.setString(1, role.getName());
            statement.setBytes(2, Utils.toBytes(role.getId()));
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRole(String roleName) {
        try {
            PreparedStatement statement = connection.prepareStatement(RoleSQL.DELETE);
            statement.setString(1, roleName);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRole(UUID userId, String role) {
        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.ADD_ROLE_TO_USER);
            statement.setBytes(1, Utils.toBytes(userId));
            statement.setString(2, role);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeRole(UUID userId, String role) {
        try {
            PreparedStatement statement = connection.prepareStatement(UserSQL.REMOVE_ROLE_FROM_USER);
            statement.setBytes(1, Utils.toBytes(userId));
            statement.setString(2, role);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
