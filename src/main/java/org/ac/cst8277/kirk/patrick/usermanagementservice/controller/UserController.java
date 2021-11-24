package org.ac.cst8277.kirk.patrick.usermanagementservice.controller;

import org.ac.cst8277.kirk.patrick.usermanagementservice.MessageServiceAPI;
import org.ac.cst8277.kirk.patrick.usermanagementservice.Utils;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Response;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.MySQLUserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.UserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.SubscriberList;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.User;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.UserRole;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private static final String PUBLISHER_ROLE = "Publisher";

    @PostMapping(value = "/users")
    public ResponseEntity<Response> createUser(@RequestBody User user) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        user.generateId();
        user.setPassword(user.getPassword());

        database.insertUser(user);
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");
        response.setData(user);

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PutMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateUser(@RequestBody User user) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        user.setPassword(user.getPassword());

        database.updateUser(user);
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");
        response.setData(user);

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping(value = "/users")
    public ResponseEntity<Response> deleteUser(@RequestParam String id) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        database.deleteUser(UUID.fromString(id));
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/users")
    public ResponseEntity<Response> getAllUsers() {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        List<User> users = database.getAllUsers();

        if (users.size() > 0) {
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Success");
            response.setData(users);
        }
        else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setMessage("No users found.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/users/publishers")
    public ResponseEntity<Response> getPublishers() {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        List<User> users = database.getPublishers();

        if (users.size() > 0) {
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Success");
            response.setData(users);
        }
        else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setMessage("No users found.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/users/subscribers")
    public ResponseEntity<Response> getSubscribers() {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        List<User> users = database.getSubscribers();

        if (users.size() > 0) {
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Success");
            response.setData(users);
        }
        else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setMessage("No users found.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/users/subscribers/{id}")
    public ResponseEntity<Response> getSubscribersTo(@PathVariable String id) {
        Response response = new Response();

        UUID publisherId = UUID.fromString(id);

        List<UUID> ids = new MessageServiceAPI()
                .getSubscribersTo(publisherId);

        if (ids != null && ids.size() > 0) {
            UserDatabase database = new MySQLUserDatabase();
            database.open();

            User publisher = database.getUserById(publisherId);

            if (publisher != null) {
                List<User> subscribers = database.getManyById(ids);

                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("Success");
                response.setData(new SubscriberList(publisher, subscribers));
            }
            database.close();
        }
        else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setMessage("No users found.");
        }

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/users/find")
    public ResponseEntity<Response> getUser(@RequestParam(required = false) String username,
                                                  @RequestParam(required = false) String id) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        User user = null;

        if (id != null) {
            user = database.getUserById(UUID.fromString(id));
        }
        else if (username != null) {
            user = database.getUserByUsername(username);
        }

        if (user != null) {
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Success");
            response.setData(user);
        }
        else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setMessage("No user found.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping(value = "/users/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> addRoleToUser(@RequestBody UserRole userRole) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        database.addRole(userRole.getId(), userRole.getRole());
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");

        if (userRole.getRole().equals(PUBLISHER_ROLE)) {
            MessageServiceAPI.notifyPublisherAdded(userRole.getId());
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping(value = "/users/role")
    public ResponseEntity<Response> removeRoleFromUser(@RequestParam UUID id, @RequestParam String role) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        database.removeRole(id, role);
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");

        if (role.equals(PUBLISHER_ROLE)) {
            MessageServiceAPI.notifyPublisherRemoved(id);
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
