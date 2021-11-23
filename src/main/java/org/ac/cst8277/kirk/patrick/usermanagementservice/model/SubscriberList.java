package org.ac.cst8277.kirk.patrick.usermanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberList {
    private User publisher;
    private List<User> subscribers;

    public User getPublisher() {
        return publisher;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }
}
