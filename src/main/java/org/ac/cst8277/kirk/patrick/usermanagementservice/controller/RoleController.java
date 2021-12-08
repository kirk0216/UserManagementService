package org.ac.cst8277.kirk.patrick.usermanagementservice.controller;

import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Response;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.MySQLUserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.UserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Role;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Session;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RoleController {
    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createRole(@RequestBody Role role) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(role.getToken());

        if (session != null) {
            User user = database.getUserById(session.getUserId());

            if (user != null) {
                role.generateId();

                database.createRole(role);
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("Success");
                response.setData(role);
            } else {
                response.setHttpStatus(HttpStatus.UNAUTHORIZED);
                response.setMessage("Invalid token.");
            }
        } else {
            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Invalid token.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateRole(@RequestBody Role role) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(role.getToken());

        if (Session.isValid(session)) {
            User user = database.getUserById(session.getUserId());

            if (user != null) {
                database.updateRole(role);
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("Success");
                response.setData(role);
            } else {
                response.setHttpStatus(HttpStatus.UNAUTHORIZED);
                response.setMessage("Invalid token.");
            }
        } else {
            if (session != null) {
                database.deleteSession(session);
            }

            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Invalid token.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping(value = "/role/{name}")
    public ResponseEntity<Response> deleteRole(@PathVariable String name, @RequestParam String token) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(UUID.fromString(token));

        if (Session.isValid(session)) {
            User user = database.getUserById(session.getUserId());

            if (user != null) {
                database.deleteRole(name);
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("Success");
            } else {
                response.setHttpStatus(HttpStatus.UNAUTHORIZED);
                response.setMessage("Invalid token.");
            }
        } else {
            if (session != null) {
                database.deleteSession(session);
            }

            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Invalid token.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
