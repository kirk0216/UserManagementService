package org.ac.cst8277.kirk.patrick.usermanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AuthorizedRequest {
    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public void generateId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public String getName() {
        return name;
    }
}
