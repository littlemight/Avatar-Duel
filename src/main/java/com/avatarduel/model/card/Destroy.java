package com.avatarduel.model.card;

import com.avatarduel.model.Element;

/**
 * Destroy Skill class that extends SKill
 */
public class Destroy extends Skill {
    /**
     * Create a new Destroy card
     * @param name card name
     * @param description card description
     * @param element card element
     * @param IMG_PATH card image path
     * @param power card power
     */
    public Destroy(String name, String description, Element element, String IMG_PATH, int power) {
        super(name, description, element, IMG_PATH, power);
    }
}
