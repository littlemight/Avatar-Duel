package com.avatarduel.model.card;

import com.avatarduel.model.Element;

/**
 * Character card that extends abstract class Card
 */
public class Character extends Card implements Summonable {
    private int atk;
    private int def;
    private int power;

    /**
     * Default constructor, calls the superclass default constructor
     */
    public Character() {
        super();
    }

    /**
     * Creates a new Character card
     * @param name card name
     * @param description card description
     * @param element card element
     * @param IMG_PATH card image path
     * @param power card power
     * @param atk character attack
     * @param def character defense
     */
    public Character(String name, String description, Element element, String IMG_PATH, int power, int atk, int def) {
        super(name, description, element, IMG_PATH);
        this.atk = atk;
        this.def = def;
        this.power = power;
    }

    /**
     * Getter for attack
     * @return character attack value
     */
    public int getAtk() {
        return this.atk;
    }

    /**
     * Getter for defense
     * @return character defense value
     */
    public int getDef() {
        return this.def;
    }

    /**
     * Getter for power
     * @return card power value
     */
    public int getPower() {
        return this.power;
    }

    /**
     * Getter for element, calls the superclass getter
     * @return card element
     */
    public Element getElement() {
        return super.getElement();
    }
}
