package org.ac.cst8277.kirk.patrick.usermanagementservice.controller;

import org.ac.cst8277.kirk.patrick.usermanagementservice.MessageServiceAPI;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.MySQLUserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.dao.UserDatabase;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Response;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Session;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.SubscriberList;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    @PostMapping(value = "/users")
    public ResponseEntity<Response> createUser(@RequestBody User user) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        user.generateId();

        database.insertUser(user);

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Success");
        response.setData(user);

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable String id, @RequestParam String token) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(UUID.fromString(token));

        if (Session.isValid(session)) {
            User user = database.getUserById(session.getUserId());

            // Users can only delete their own account.
            if (user != null && user.getId() == UUID.fromString(id)) {
                database.deleteUser(UUID.fromString(id));

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
    
    @GetMapping(value = "/users")
    public ResponseEntity<Response> getAllUsers(@RequestParam String token) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(UUID.fromString(token));

        if (Session.isValid(session)) {
            User user = database.getUserById(session.getUserId());

            if (user != null) {
                List<User> users = database.getAllUsers();

                if (users.size() > 0) {
                    response.setHttpStatus(HttpStatus.OK);
                    response.setMessage("Success");
                    response.setData(users);
                } else {
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    response.setMessage("No users found.");
                }
            } else {
                response.setHttpStatus(HttpStatus.UNAUTHORIZED);
                response.setMessage("Invalid token.");
            }
        }
        else {
            if (session != null) {
                database.deleteSession(session);
            }

            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Invalid token.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/users/publishers")
    public ResponseEntity<Response> getPublishers(@RequestParam String token) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(UUID.fromString(token));

        if (Session.isValid(session)) {
            User user = database.getUserById(session.getUserId());

            if (user != null) {
                List<User> users = database.getPublishers();

                if (users.size() > 0) {
                    response.setHttpStatus(HttpStatus.OK);
                    response.setMessage("Success");
                    response.setData(users);
                } else {
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    response.setMessage("No users found.");
                }
            } else {
                response.setHttpStatus(HttpStatus.UNAUTHORIZED);
                response.setMessage("Invalid token.");
            }
        }
        else {
            if (session != null) {
                database.deleteSession(session);
            }

            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Invalid token.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/users/subscribers")
    public ResponseEntity<Response> getSubscribers(@RequestParam String token) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(UUID.fromString(token));

        if (Session.isValid(session)) {
            User user = database.getUserById(session.getUserId());

            if (user != null) {
                List<User> users = database.getSubscribers();

                if (users.size() > 0) {
                    response.setHttpStatus(HttpStatus.OK);
                    response.setMessage("Success");
                    response.setData(users);
                } else {
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    response.setMessage("No users found.");
                }
            } else {
                response.setHttpStatus(HttpStatus.UNAUTHORIZED);
                response.setMessage("User is not authenticated.");
            }
        } else {
            if (session != null) {
                database.deleteSession(session);
            }

            response.setHttpStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("User is not authenticated.");
        }

        database.close();

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping(value = "/users/subscribers/{id}")
    public ResponseEntity<Response> getSubscribersTo(@PathVariable String id, @RequestParam String token) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(UUID.fromString(token));

        if (Session.isValid(session)) {
            User user = database.getUserById(session.getUserId());

            if (user != null) {
                UUID publisherId = UUID.fromString(id);

                List<UUID> ids = MessageServiceAPI.getSubscribersTo(publisherId);

                if (ids != null && ids.size() > 0) {
                    User publisher = database.getUserById(publisherId);

                    if (publisher != null) {
                        List<User> subscribers = database.getManyById(ids);

                        response.setHttpStatus(HttpStatus.OK);
                        response.setMessage("Success");
                        response.setData(new SubscriberList(publisher, subscribers));
                    }
                } else {
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    response.setMessage("No users found.");
                }
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

    @GetMapping(value = "/users/find")
    public ResponseEntity<Response> getUser(@RequestParam(required = false) String username,
                                            @RequestParam(required = false) String id,
                                            @RequestParam String token) {
        Response response = new Response();

        UserDatabase database = new MySQLUserDatabase();
        database.open();

        Session session = database.getSessionForToken(UUID.fromString(token));

        if (Session.isValid(session)) {
            User tokenUser = database.getUserById(session.getUserId());

            if (tokenUser != null) {
                User user = null;

                if (id != null) {
                    user = database.getUserById(UUID.fromString(id));
                } else if (username != null) {
                    user = database.getUserByUsername(username);
                }

                if (user != null) {
                    response.setHttpStatus(HttpStatus.OK);
                    response.setMessage("Success");
                    response.setData(user);
                } else {
                    response.setHttpStatus(HttpStatus.NOT_FOUND);
                    response.setMessage("No user found.");
                }
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
