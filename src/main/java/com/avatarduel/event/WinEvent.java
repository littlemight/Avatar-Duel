package com.avatarduel.event;

import com.avatarduel.model.Player;

/**
 * Event class for win condition
 */
public class WinEvent implements Event {
    private Player player;

    /**
     * Constructor for WinEvent
     * @param player the player that win the game
     * @see Player
     */
    public WinEvent(Player player) {
        this.player = player;
    }

    /**
     * Getter for Player player
     * @return reference to {@link Player} object contained in this event.
     * @see Player
     */
    @Override
    public Object getInfo() {
        return this.player;
    }
}