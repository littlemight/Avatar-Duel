package com.avatarduel.model.card;

import com.avatarduel.model.Element;

/**
 * Aura card that extends Skill
 */
public class PowerUp extends Skill {
    /**
     * Create a new PowerUp card
     * @param name card name
     * @param description card description
     * @param element card element
     * @param IMG_PATH card img path
     * @param power card power
     */
    public PowerUp(String name, String description, Element element, String IMG_PATH, int power) {
        super(name, description, element, IMG_PATH, power);
    }
}
