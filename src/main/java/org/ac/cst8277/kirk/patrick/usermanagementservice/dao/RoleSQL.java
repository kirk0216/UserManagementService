package org.ac.cst8277.kirk.patrick.usermanagementservice.dao;

public final class RoleSQL {
    public static final String INSERT =
            "INSERT INTO ums.role (id, name) VALUES (?, ?);";
    public static final String UPDATE =
            "UPDATE ums.role SET name = ? WHERE id = ?;";
    public static final String DELETE =
            "DELETE FROM ums.role WHERE name = ?;";
}
