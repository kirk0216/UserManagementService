package org.ac.cst8277.kirk.patrick.usermanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AuthorizedRequest {
    private UUID token;

    public UUID getToken() {
        return token;
    }
}
