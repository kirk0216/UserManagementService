package org.ac.cst8277.kirk.patrick.usermanagementservice.controller;

import org.ac.cst8277.kirk.patrick.usermanagementservice.Utils;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.MySQLUserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.UserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Response;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Session;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.User;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RestController
public class AuthController {
    @GetMapping(value = "/")
    public ResponseEntity<Response> receiveGitHubToken(@AuthenticationPrincipal OAuth2User userDetails) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        User user = database.getUserByUsername(userDetails.getAttribute("login"));

        if (user == null) {
            user = new User();
            user.setId(UUID.randomUUID());
            user.setUsername(userDetails.getAttribute("login"));
            user.setEmail(userDetails.getAttribute("email"));
            user.setPassword(null);
            database.insertUser(user);
        }

        UUID token = UUID.randomUUID();

        database.insertSession(user, token);

        database.close();

        response.setMessage(token.toString());
        response.setHttpStatus(HttpStatus.OK);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/auth/validate/{tokenString}")
    public ResponseEntity<Response> validToken(@PathVariable String tokenString) {
        Response response = new Response();

        UUID token = UUID.fromString(tokenString);

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(token);

        if (Session.isValid(session)) {
            User user = database.getUserById(session.getUserId());

            response.setMessage("OK");
            response.setHttpStatus(HttpStatus.OK);
            response.setData(user.getRoles());
        }
        else {
            if (session != null) {
                database.deleteSession(session);
            }

            response.setMessage("Invalid token.");
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
