package org.ac.cst8277.kirk.patrick.usermanagementservice.controller;

import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Response;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.MySQLUserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.UserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {
    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createRole(@RequestBody Role role) {
        Response response = new Response();

        role.generateId();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        database.createRole(role);
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");
        response.setData(role);

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateRole(@RequestBody Role role) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        database.updateRole(role);
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");
        response.setData(role);

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping(value = "/role")
    public ResponseEntity<Response> deleteRole(@RequestParam String name) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        database.deleteRole(name);
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
