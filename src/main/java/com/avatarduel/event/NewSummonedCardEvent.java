package com.avatarduel.event;

import com.avatarduel.controller.CardController;

/**
 * Event class for summoning a new card into the game action
 */
public class NewSummonedCardEvent implements Event {
    CardController info;

    /**
     * Constructor for NewSummonedCardEvent
     * @param info CardController object
     */
    public NewSummonedCardEvent(CardController info) {
        this.info = info;
    }

    /**
     * Getter for CardController referenced by this object
     * @return reference to CardController in this object
     * @see CardController
     */
    @Override
    public Object getInfo() {
        return this.info;
    }
}
