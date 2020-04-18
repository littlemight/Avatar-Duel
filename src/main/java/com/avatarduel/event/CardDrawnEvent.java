package com.avatarduel.event;

import com.avatarduel.model.card.Card;

/**
 * Event class related to cards.
 * Implements Event interface
 */
public class CardDrawnEvent implements Event {
    private Card card;

    /**
     * Constructor for CardDrawnEvent
     * @param card card object relates to this event
     * @see Card
     */
    public CardDrawnEvent(Card card) {
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
