package com.avatarduel.event;

import com.avatarduel.model.card.Card;

/**
 * Event for mouse hovering on card action
 */
public class HoverCardEvent implements Event {
    private Card card;

    /**
     * Constructor for HoverCardEvent.
     * Stores the information on hovered card
     * @param card reference to the hovered card object.
     * @see Card
     */
    public HoverCardEvent(Card card) {
        this.card = card;
    }

    /**
     * Getter for hovered card reference
     * @return reference to the hovered card, an instance of {@link Card}
     * @see Card
     */
    @Override
    public Object getInfo() {
        return this.card;
    }
}
