package org.ac.cst8277.kirk.patrick.usermanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private UUID id;
    private String role;

    public UUID getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
