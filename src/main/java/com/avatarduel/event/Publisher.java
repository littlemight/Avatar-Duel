package com.avatarduel.event;

public interface Publisher {
    void publish(Publisher publisher, Event event);
}
