package com.avatarduel.event;

import com.avatarduel.controller.SummonedCharacterController;

/**
 * Event class for character card buffed with skill being picked action
 */
public class SkillCharacterPickedEvent implements Event {
    SummonedCharacterController info;

    /**
     * Constructor for SkillCharacterPickedEvent
     * @param info reference to controller for summoned character
     */
    public SkillCharacterPickedEvent(SummonedCharacterController info) {
        this.info = info;
    }

    /**
     * Getter for this object's info
     * @return reference to SummonedCharacterController object
     */
    @Override
    public Object getInfo() {
        return this.info;
    }
}
