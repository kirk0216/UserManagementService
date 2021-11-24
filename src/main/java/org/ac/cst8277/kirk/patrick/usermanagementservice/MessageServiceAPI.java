package org.ac.cst8277.kirk.patrick.usermanagementservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.Id;
import org.ac.cst8277.kirk.patrick.usermanagementservice.model.UUIDResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

/**
 * Object for interacting with the message service API.
 */
public class MessageServiceAPI {
    private static final String SCHEME = "http";
    private static final String HOST = "messageservice";
    private static final int PORT = 8080;

    private static final String SUBSCRIBERS_FOR = "/subscribers/";
    private static final String PUBLISHER_ROLE_CHANGED = "/publisher";

    public static List<UUID> getSubscribersTo(UUID publisherId) {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .path(SUBSCRIBERS_FOR + publisherId.toString())
                .build()
                .toUri();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Accept", "application/json")
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                UUIDResponse ids = mapper.readValue(response.body(), UUIDResponse.class);

                return ids.getData();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void notifyPublisherAdded(UUID id) {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .path(PUBLISHER_ROLE_CHANGED)
                .build()
                .toUri();

        String json = null;
        Id idObj = new Id(id);

        // Convert id to a JSON string for sending.
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(idObj);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(json);

        if (json == null) {
            return;
        }

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();

        // UMS doesn't care if the message service responds, so discard any response
        HttpClient.newHttpClient()
            .sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding());
    }

    public static void notifyPublisherRemoved(UUID id) {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .path(PUBLISHER_ROLE_CHANGED + "/" + id.toString())
                .build()
                .toUri();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();

        // UMS doesn't care if the message service responds, so discard any response
        HttpClient.newHttpClient()
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding());
    }
}
