package org.ac.cst8277.kirk.patrick.usermanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ac.cst8277.kirk.patrick.usermanagementservice.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private final List<String> roles = new ArrayList<String>();

    public UUID getId() {
        return id;
    }

    public void generateId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public void removeRole(String role) {
        roles.remove(role);
    }

    public boolean hasRole(String role) {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).equals(role)) {
                return true;
            }
        }

        return false;
    }

    public static User getFromResultSet(ResultSet result) {
        User user = null;

        try {
            UUID userId = Utils.toUUID(result.getBytes("id"));
            String username = result.getString("username");
            String email = result.getString("email");
            String password = result.getString("password");

            user = new User(userId, username, email, password);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
