package com.avatarduel.model.card;

import com.avatarduel.model.Element;

/**
 * Aura card that extends Skill
 */
public class Aura extends Skill {
    private int delta_atk;
    private int delta_def;

    /**
     * Create a new Aura card
     * @param name card name
     * @param description card description
     * @param element card element
     * @param IMG_PATH card img path
     * @param power card power
     * @param delta_atk aura delta attack
     * @param delta_def aura delta defense
     */
    public Aura(String name, String description, Element element, String IMG_PATH, int power, int delta_atk, int delta_def) {
        super(name, description, element, IMG_PATH, power);
        this.delta_atk = delta_atk;
        this.delta_def = delta_def;
    }

    /**
     * Get the delta attack of Aura card
     * @return delta attack value
     */
    public int getDeltaAtk() {
        return this.delta_atk;
    }

    /**
     * Get the delta defense of Aura card
     * @return delta defense value
     */
    public int getDeltaDef() {
        return this.delta_def;
    }
}
