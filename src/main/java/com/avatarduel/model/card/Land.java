package com.avatarduel.model.card;

import com.avatarduel.model.Element;

/**
 * Land class that extends Card
 * This card increases the power pool according to its element.
 */
public class Land extends Card {
    /**
     * Create a new Land card
     * @param name card name
     * @param description card description
     * @param element card element
     * @param IMG_PATH card img path
     */
    public Land(String name, String description, Element element, String IMG_PATH) {
        super(name, description, element, IMG_PATH);
    }
}
