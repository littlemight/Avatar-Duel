package com.avatarduel.event;

import com.avatarduel.controller.CardController;

/**
 * Event class for new card being drawn from deck action
 */
public class NewCardDrawnEvent implements Event {
    CardController new_card_controller;

    /**
     * Constructor for NewCardDrawnEvent
     * @param new_card_controller reference to CardController object which the new card being drawn into.
     * @see CardController
     */
    public NewCardDrawnEvent(CardController new_card_controller) {
        this.new_card_controller = new_card_controller;
    }

    /**
     * Getter for the card controller which new card from deck being drawn into.
     * @return reference to CardController referenced by this event object.
     */
    @Override
    public Object getInfo() {
        return this.new_card_controller;
    }
}
