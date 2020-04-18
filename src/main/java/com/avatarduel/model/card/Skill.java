package com.avatarduel.model.card;

import com.avatarduel.model.Element;

/**
 * Abstract Class Skill that extends Card, this serves as a boilerplate for all type of Skill cards
 */
public abstract class Skill extends Card implements Summonable {
    private int power;

    /**
     * Default constructor
     */
    public Skill() {
        super();
        this.power = 0;
    }

    public Skill(String name, String description, Element element, String IMG_PATH, int power) {
        super(name, description, element, IMG_PATH);
        this.power = power;
    }

    /**
     * Getter for card power
     * @return card power value
     */
    public int getPower() {
        return this.power;
    }

    /**
     * Getter for card element
     * @return card element
     */
    public Element getElement() {
        return super.getElement();
    }
}
