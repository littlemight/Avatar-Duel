package com.avatarduel.event;

import com.avatarduel.controller.BoardController;
import com.avatarduel.model.Phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A channel that handles the event on Board component.
 */
public class BoardChannel implements EventChannel {
    private Map<Publisher, List<Subscriber>> subscribers;
    private BoardController main;
    private Phase phase;
    private int player_id;

    /**
     * Default constructor
     */
    public BoardChannel() {
        subscribers = new HashMap<Publisher, List<Subscriber>>();
        phase = Phase.DRAW;
    }

    /**
     * Getter for BoardController main component
     * @return BoardController main
     * @see BoardController
     */
    public Object getMain() {
        return this.main;
    }

    /**
     * Getter for Phase phase
     * @return Phase phase
     * @see Phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Getter for the current player_id
     * @return int player_id
     */
    public int getPlayerID() {
        return this.player_id;
    }

    /**
     * Set the phase of the current object with given parameter
     * @param phase reference to phase to set
     * @see Phase
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * Setter for this object's player_id
     * @param id the id to set to this object
     */
    public void setPlayerID(int id) {
        this.player_id = id;
    }

    /**
     * Setter for this object's board controller
     * @param main set this object's BoardController reference to the given parameter
     * @see BoardController
     */
    public void setMain(Object main) {
        this.main = (BoardController) main;
    }

    /**
     * Implementation of sendEvent method from EventChannel interface.
     * Sends {@link Event} object to every subscriber that subscribes to {@link Publisher}.
     * Prints error message when exception was thrown from the subscribers
     * @param publisher Specifies the publisher to publish the event
     * @param event the event that will be sent to publisher's subscribers
     * @see Publisher
     * @see Event
     * @see Subscriber
     */
    @Override
    public void sendEvent(Publisher publisher, Event event) {
        try {
            if (subscribers.get(publisher) == null) {
                return;
            }
            for (Subscriber s: subscribers.get(publisher)) {
                s.onEvent(event);
            }
        } catch (Exception e) {
            System.out.println("IN CHANNEL: " + e);
        }
    }

    /**
     * Adds {@link Publisher} to the current channel if not exists already.
     * Ignores if the publisher in parameter already exist.
     * @param publisher publisher object to be added into the pool
     * @see Publisher
     */
    @Override
    public void addPublisher(Publisher publisher) {
        subscribers.putIfAbsent(publisher, new ArrayList<Subscriber>());
    }

    /**
     * Remove a publisher/subscriber from the channel.
     * Removes a publisher if the object implements Publisher interface.
     * Removes a subscriber if the object implements Subscriber interface.
     * @param o object that want to be removed from the channel.
     * @see Publisher
     */
    @Override
    public void removeComponent(Object o) {
        if (subscribers.get(o) == null) {
            return;
        }
        subscribers.remove(o);
        for (Map.Entry<Publisher, List<Subscriber>> entry: subscribers.entrySet()) {
            if (entry.getValue().contains(o)) {
                entry.getValue().remove(o);
            }
        }
    }

    /**
     * Adds a subscriber to a publisher.
     * Adds a publisher first if there is no publisher in the channel.
     * @param publisher Publisher that been subscribed by the subscriber
     * @param subscriber Subscriber object which to be added into the channel
     * @see Publisher
     * @see Subscriber
     */
    @Override
    public void addSubscriber(Publisher publisher, Subscriber subscriber) {
        addPublisher(publisher);
        subscribers.get(publisher).add(subscriber);
    }
}
