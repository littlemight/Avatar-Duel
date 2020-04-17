package com.avatarduel.event;

import com.avatarduel.model.Player;

public class WinEvent implements Event {
    private Player player;

    public WinEvent(Player player) {
        this.player = player;
    }

    @Override
    public Object getInfo() {
        return this.player;
    }
}