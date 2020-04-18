package com.avatarduel.event;

/**
 * Event class for changing player action
 */
public class PlayerChangedEvent implements Event {
    private int cur_player;

    /**
     * Constructor for PlayerChangedEvent
     * @param cur_player player_id that being changed into.
     */
    public PlayerChangedEvent(int cur_player) {
        this.cur_player = cur_player;
    }

    /**
     * Getter for the player_id referenced by this event.
     * @return player_id referenced by this event object.
     */
    @Override
    public Object getInfo() {
        return this.cur_player;
    }
}
