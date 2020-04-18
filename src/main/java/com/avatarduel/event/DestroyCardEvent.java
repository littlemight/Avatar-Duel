package com.avatarduel.event;

import com.avatarduel.model.card.Card;

/**
 * Event class for Destroy Card action
 */
public class DestroyCardEvent implements Event {
    private Card card;

    /**
     * Constructor for DestroyCardEvent
     * @param card card object relates to this event
     */
    public DestroyCardEvent(Card card) {
        this.card = card;
    }

    /**
     * Getter for Card card
     * @return reference for this event's card object.
     * @see Card
     */
    @Override
    public Object getInfo() {
        return this.card;
    }
}
