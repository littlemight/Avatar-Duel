package com.avatarduel.event;

import com.avatarduel.model.card.Summoned;

/**
 * Event class for hover action on summoned card
 */
public class HoverSummonedCardEvent implements Event {
    Summoned summoned_card;

    /**
     * Constructor for HoverSummonedCardEvent.
     * @param summoned_card an instance of {@link Summoned} card.
     */
    public HoverSummonedCardEvent(Summoned summoned_card) {
        this.summoned_card = summoned_card;
    }

    /**
     * Getter for hovered summonedcard reference
     * @return reference of summoned card that being hovered.
     */
    public Object getInfo() {
        return this.summoned_card;
    }
}
