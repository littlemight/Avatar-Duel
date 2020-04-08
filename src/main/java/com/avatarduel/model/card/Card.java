package com.avatarduel.model.card;

import com.avatarduel.model.Element;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Card {
    private String name;
    private String description;
    private Element element;
    private String IMG_PATH;

    public Card() {
        this.name = "";
        this.description = "";
        this.element = Element.AIR;
        this.IMG_PATH = "card/image/placeholder.png";
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

    public Element getElement() { return this.element; }

    public String getIMGPath() {
        return this.IMG_PATH;
    }
}
