package org.ac.cst8277.kirk.patrick.usermanagementservice.controller;

import org.ac.cst8277.kirk.patrick.usermanagementservice.Utils;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.MySQLUserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.UserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Response;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.User;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @PostMapping(value = "/auth/login")
    public ResponseEntity<Response> login(@RequestBody UserCredentials credentials) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        User user = database.getUserByUsername(credentials.getUsername());

        if (user.getPassword().equals(credentials.getPassword())) {
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Success");
            response.setData(user);
        }
        else {
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Invalid login credentials.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
