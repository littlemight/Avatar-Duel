package com.avatarduel.event;

import com.avatarduel.controller.CardController;
import com.avatarduel.model.card.SummonedSkill;

/**
 * Event class for new skill card being placed in the field.
 */
public class NewSkillCardPlaced implements Event {
    SummonedSkill info;

    /**
     * Constructor for NewSkillCardPlaced.
     * @param info reference to skill card that being summoned into the field.
     */
    public NewSkillCardPlaced(SummonedSkill info) {
        this.info = info;
    }

    /**
     * Getter for reference to summoned skill card.
     * @return reference to summoned skill card on this event's object.
     */
    @Override
    public Object getInfo() {
        return this.info;
    }
}
