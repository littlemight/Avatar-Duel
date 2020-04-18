package com.avatarduel.model.card;

import com.avatarduel.model.Element;

/**
 * Abstract Class that serves as a boilerplate for all Cards
 */
public abstract class Card {
    private String name;
    private String description;
    private Element element;
    private String IMG_PATH;

    /**
     * Default constructor
     */
    public Card() {
        this.name = "";
        this.description = "";
        this.element = Element.AIR;
        this.IMG_PATH = "card/image/placeholder.png";
    }

    /**
     * Create a new Card
     * @param name name of the card
     * @param description description of the card
     * @param element element of the card
     * @param IMG_PATH image path of the card from CSV
     */
    public Card(String name, String description, Element element, String IMG_PATH) {
        this.name = name;
        this.description = description;
        this.element = element;
        this.IMG_PATH = IMG_PATH;
    }

    /**
     * Getter for card name
     * @return card name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for card description
     * @return card description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for element
     * @return card element
     */
    public Element getElement() { return this.element; }

    /**
     * Getter for card image
     * @return card image path
     */
    public String getIMGPath() {
        return this.IMG_PATH;
    }
}
