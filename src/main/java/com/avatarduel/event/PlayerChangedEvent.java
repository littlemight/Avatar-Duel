package com.avatarduel.event;

public class PlayerChangedEvent implements Event {
    private int cur_player;

    public PlayerChangedEvent(int cur_player) {
        this.cur_player = cur_player;
    }

    @Override
    public Object getInfo() {
        return this.cur_player;
    }
}
