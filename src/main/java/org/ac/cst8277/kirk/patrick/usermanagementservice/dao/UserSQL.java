package org.ac.cst8277.kirk.patrick.usermanagementservice.dao;

public final class UserSQL {
    public static final String INSERT =
            "INSERT INTO ums.user (id, username, email, password) VALUES (?, ?, ?, ?);";
    public static final String UPDATE =
            "UPDATE ums.user SET username = ?, email = ?, password = ? WHERE id = ?;";
    public static final String DELETE =
            "DELETE FROM ums.user WHERE id = ?;";

    public static final String GET_ALL =
            "SELECT u.id, u.username, u.email, u.password, u.created, " +
            "r.name AS role_name FROM ums.user u " +
            "LEFT JOIN ums.user_role ur ON ur.user_id = u.id " +
            "LEFT JOIN ums.role r ON r.id = ur.role_id;";
    public static final String GET_BY_ID =
            "SELECT u.id, u.username, u.email, u.password, u.created, " +
            "r.name AS role_name FROM ums.user u " +
            "LEFT JOIN ums.user_role ur ON ur.user_id = u.id " +
            "LEFT JOIN ums.role r ON r.id = ur.role_id " +
            "WHERE u.id = ?;";
    public static final String GET_BY_USERNAME =
            "SELECT u.id, u.username, u.email, u.password, u.created, " +
            "r.name AS role_name FROM ums.user u " +
            "LEFT JOIN ums.user_role ur ON ur.user_id = u.id " +
            "LEFT JOIN ums.role r ON r.id = ur.role_id " +
            "WHERE u.username = ?;";
    public static final String GET_PUBLISHERS =
            "SELECT u.id, u.username, u.email, u.password, u.created, " +
            "r.name AS role_name FROM ums.user u " +
            "LEFT JOIN ums.user_role ur ON ur.user_id = u.id " +
            "LEFT JOIN ums.role r ON r.id = ur.role_id " +
            "WHERE r.name = 'Publisher';";
    public static final String GET_SUBSCRIBERS =
            "SELECT u.id, u.username, u.email, u.password, u.created, " +
            "r.name AS role_name FROM ums.user u " +
            "LEFT JOIN ums.user_role ur ON ur.user_id = u.id " +
            "LEFT JOIN ums.role r ON r.id = ur.role_id " +
            "WHERE r.name = 'Subscriber';";
    public static final String GET_SUBSCRIBERS_TO =
            "SELECT u.id, u.username, u.email, u.password, u.created, " +
            "r.name AS role_name FROM ums.user u " +
            "LEFT JOIN ums.user_role ur ON ur.user_id = u.id " +
            "LEFT JOIN ums.role r ON r.id = ur.role_id " +
            "WHERE u.id = ?;";

    public static final String ADD_ROLE_TO_USER =
            "INSERT INTO ums.user_role (user_id, role_id) VALUES (?, (SELECT id FROM ums.role WHERE name = ?));";
    public static final String REMOVE_ROLE_FROM_USER =
            "DELETE FROM ums.user_role WHERE user_id = ? AND role_id = (SELECT id FROM ums.role WHERE name = ?);";
}
