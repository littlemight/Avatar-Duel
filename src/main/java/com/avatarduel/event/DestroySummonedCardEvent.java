package com.avatarduel.event;

import com.avatarduel.model.card.Summoned;

/**
 * Event class for destroy summoned character card action
 */
public class DestroySummonedCardEvent implements Event {
    private Summoned summoned;

    /**
     * Constructor for DestroySummonedCardEvent
     * @param summoned an character card object that implements {@link Summoned} interface
     */
    public DestroySummonedCardEvent(Summoned summoned) {
        this.summoned = summoned;
    }

    /**
     * Getter for the summoned card on this event object
     * @return reference to summoned card
     */
    @Override
    public Object getInfo() {
        return this.summoned;
    }
}
