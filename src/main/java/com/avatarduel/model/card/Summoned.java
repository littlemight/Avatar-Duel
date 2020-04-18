package com.avatarduel.model.card;

import com.avatarduel.event.BoardChannel;

/**
 * Interface for the Summoned card.
 * This interface will be implemented for SummonedSkill and SummonedCharacter,
 * since they have different behavior when on the player hand and the player field
 */
public interface Summoned {
    /**
     * Get the base card without the Summoned interface
     * @return card
     */
    Card getBaseCard();

    /**
     * Remove this card from the board and player, this publishes a DestroySummonedCardEvent
     */
    void removeCard();

    /**
     * Set the channel for this model
     * @param channel connected channel
     */
    void setChannel(BoardChannel channel);
}
