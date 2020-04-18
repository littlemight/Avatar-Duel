package com.avatarduel.event;

/**
 * An interface to handle events from publisher and subscriber.
 * Every class that implements this interface must implement the following methods.
 */
public interface EventChannel {
    public void sendEvent(Publisher publisher, Event event);

    public void addPublisher(Publisher publisher);

    public void removeComponent(Object o);

    public void addSubscriber(Publisher publisher, Subscriber subscriber);
}
