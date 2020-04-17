package com.avatarduel.event;

public interface EventChannel {
    public void sendEvent(Publisher publisher, Event event);

    public void addPublisher(Publisher publisher);

    public void removeComponent(Object o);

    public void addSubscriber(Publisher publisher, Subscriber subscriber);
}
