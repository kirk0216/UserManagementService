package org.ac.cst8277.kirk.patrick.usermanagementservice;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<UUID> getSubscribersTo(UUID publisherId) {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .port(PORT)
                .path(SUBSCRIBERS_FOR + publisherId.toString())
                .build()
                .toUri();

        System.out.println(uri.toString());

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Accept", "application/json")
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

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
}
