package com.avatarduel.model.card;

import com.avatarduel.event.BoardChannel;
import com.avatarduel.event.DestroySummonedCardEvent;
import com.avatarduel.event.Event;
import com.avatarduel.event.Publisher;

/**
 * A Summoned class for Skill
 */
public class SummonedSkill implements Summoned, Publisher {
    private Skill card;
    private SummonedCharacter applied_to;
    BoardChannel channel;

    /**
     * Create a new summoned skill card
     * @param card skill card
     */
    public SummonedSkill(Skill card) {
        this.card = card;
        applied_to = null;
    }

    /**
     * Set which card this skill is applied to
     * @param applied_to the applied character card
     */
    public void setAppliedTo(SummonedCharacter applied_to) {
        this.applied_to = applied_to;
    }

    /**
     * Get the base card without the Summoned interface
     * @return card
     */
    public Card getBaseCard() {
        return this.card;
    }

    /**
     * Get the applied character card of this skill
     * @return summoned character card
     */
    public SummonedCharacter getAppliedTo() {
        return this.applied_to;
    }

    public void setChannel(BoardChannel channel) {
        this.channel = channel;
    }

    public void removeCard() {
        this.applied_to.removeSkill(this);
        publish(new DestroySummonedCardEvent(this));
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }
}
