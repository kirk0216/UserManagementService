package org.ac.cst8277.kirk.patrick.usermanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private UUID userId;
    private UUID token;
    private Timestamp created;

    public static boolean isValid(Session session) {
        if (session != null) {
            Duration duration = Duration.between(Instant.now(), session.getCreated().toInstant());

            return duration.toMinutes() < 15;
        }

        return false;
    }
}
