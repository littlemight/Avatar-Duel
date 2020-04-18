package com.avatarduel.event;

import com.avatarduel.model.Player;

/**
 * Event class for player being selected action
 */
public class PlayerSelectedEvent implements Event {
    private Player player;

    /**
     * Constructor for PlayerSelectedEvent
     * @param player reference to Player object that being selected
     * @see Player
     */
    public PlayerSelectedEvent(Player player) {
        this.player = player;
    }

    /**
     * Getter for player object referenced by this event.
     * @return reference to player object referenced by this event object.
     * @see Player
     */
    @Override
    public Object getInfo() {
        return this.player;
    }
}