package com.avatarduel.event;

import com.avatarduel.model.Player;

public class PlayerSelectedEvent implements Event {
    private Player player;

    public PlayerSelectedEvent(Player player) {
        this.player = player;
    }

    @Override
    public Object getInfo() {
        return this.player;
    }
}