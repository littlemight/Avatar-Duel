package com.avatarduel.model.card;

import com.avatarduel.model.Element;

public abstract class Card {
    private int id; // ???? keknya ga perlu
    private String name;
    private String description;
    private Element element;
    private String IMG_PATH;

    // Will make builder class
    public Card() {
        this.name = "";
        this.description = "";
        this.element = Element.AIR;
    }

    public Card(String name, String description, Element element, String IMG_PATH) {
        this.name = name;
        this.description = description;
        this.element = element;
        this.IMG_PATH = IMG_PATH;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Element getElement() {
        return this.element;
    }

    public String getIMGPath() {
        return this.IMG_PATH;
    }
}
