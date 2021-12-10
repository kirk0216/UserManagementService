package org.ac.cst8277.kirk.patrick.usermanagementservice.dao;

public final class SessionSQL {
    public static final String INSERT =
            "INSERT INTO ums.session (user_id, token) VALUES (?, ?);";
    public static final String DELETE =
            "DELETE FROM ums.session WHERE user_id = ? AND token = ?;";
    public static final String GET_BY_TOKEN =
            "SELECT * FROM ums.session WHERE token = ?;";
    public static final String GET_BY_USER_ID =
            "SELECT * FROM ums.session WHERE user_id = ?;";
}
