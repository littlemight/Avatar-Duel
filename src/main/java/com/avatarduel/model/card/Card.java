package com.avatarduel.model.card;

import com.avatarduel.model.Element;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Card {
    private StringProperty name;
    private StringProperty description;
    private ObjectProperty<Element> element;
    private StringProperty IMG_PATH;

    public Card() {
        this.name = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.element = new SimpleObjectProperty<>(Element.AIR);
        this.IMG_PATH = new SimpleStringProperty("card/image/placeholder.png");
    }

    public Card(String name, String description, Element element, String IMG_PATH) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.element = new SimpleObjectProperty<Element>(element);
        this.IMG_PATH = new SimpleStringProperty(IMG_PATH);
    }

    public StringProperty getNameProperty() {return this.name;};

    public String getName() {
        return this.name.getValue();
    }

    public StringProperty getDescriptionProperty() {
        return this.description;
    }
    public String getDescription() {
        return this.description.getValue();
    }

    public ObjectProperty<Element> getElementProperty() {
        return this.element;
    }
    public Element getElement() { return this.element.getValue(); }

    public StringProperty getIMGPathProperty() {
        return this.IMG_PATH;
    }
    public String getIMGPath() {
        return this.IMG_PATH.getValue();
    }
}
