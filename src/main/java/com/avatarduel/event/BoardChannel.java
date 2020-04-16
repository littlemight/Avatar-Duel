package com.avatarduel.event;

import com.avatarduel.controller.BoardController;
import com.avatarduel.model.Phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardChannel implements EventChannel {
    private Map<Publisher, List<Subscriber>> subscribers;
    private BoardController main;
    private Phase phase;
    private int player_id;

    public BoardChannel() {
        subscribers = new HashMap<Publisher, List<Subscriber>>();
    }

    public Object getMain() {
        return this.main;
    }

    public Phase getPhase() {
        return phase;
    }

    public int getPlayerID() {
        return this.player_id;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public void setPlayerID(int id) {
        this.player_id = id;
    }

    public void setMain(Object main) {
        this.main = (BoardController) main;
    }

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

    @Override
    public void addPublisher(Publisher publisher) {
        subscribers.putIfAbsent(publisher, new ArrayList<Subscriber>());
    }

    @Override
    public void addSubscriber(Publisher publisher, Subscriber subscriber) {
        addPublisher(publisher);
        subscribers.get(publisher).add(subscriber);
    }
}
