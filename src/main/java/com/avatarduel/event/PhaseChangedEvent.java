package com.avatarduel.event;

import com.avatarduel.model.Phase;

/**
 * Event class for changing game phase action
 */
public class PhaseChangedEvent implements Event {
    private Phase cur_phase;

    /**
     * Constructor for PhaseChangedEvent
     * @param cur_phase reference to {@link Phase} object on this event.
     */
    public PhaseChangedEvent(Phase cur_phase) {
        this.cur_phase = cur_phase;
    }

    /**
     * Getter for the phase object referenced by this event.
     * @return reference to {@link Phase} object referenced by this event object.
     */
    @Override
    public Object getInfo() {
        return this.cur_phase;
    }
}
