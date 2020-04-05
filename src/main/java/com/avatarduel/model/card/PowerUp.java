package com.avatarduel.model.card;

import com.avatarduel.model.Element;

public class PowerUp extends Skill {
    public PowerUp() {
        super();
    }

    public PowerUp(String name, String description, Element element, String IMG_PATH, int power) {
        super(name, description, element, IMG_PATH, power);
    }
}
