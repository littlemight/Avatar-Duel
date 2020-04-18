package com.avatarduel.model.card;

import com.avatarduel.model.Element;

/**
 * Summonable interface for a card that can be summoned (being put into the field).
 * Land is not a summonable card since it is not "summoned", but being put into the element zone to increase a Player's power pool.
 */
public interface Summonable {
    /**
     * Get the element of the card
     * @return card element
     */
    Element getElement();

    /**
     * Get the power of the card
     * @return power element
     */
    int getPower();
}
