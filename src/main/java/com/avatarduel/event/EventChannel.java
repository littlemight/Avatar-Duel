package com.avatarduel.event;

public interface EventChannel {

    public Object getMain();
    public void setMain(Object main);

    public void sendEvent(Publisher publisher, Event event);

    public void addPublisher(Publisher publisher);

    public void addSubscriber(Publisher publisher, Subscriber subscriber);
}
